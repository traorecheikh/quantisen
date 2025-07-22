package com.quantisen.boisson.infrastructure.web;

import com.quantisen.boisson.application.identite.dtos.IdentiteDto;
import com.quantisen.boisson.application.identite.requests.IdentiteCredentialsRequest;
import com.quantisen.boisson.application.identite.requests.LoginResponse;
import com.quantisen.boisson.application.identite.requests.PasswordRequest;
import com.quantisen.boisson.application.identite.requests.StatusDto;
import com.quantisen.boisson.application.identite.services.IdentiteService;
import com.quantisen.boisson.infrastructure.security.AllowedRoles;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/utilisateurs")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IdentiteController {
    @Inject
    private IdentiteService utilisateurService;

    @GET
    @AllowedRoles({"GERANT"})
    public Response getAll() {
        try {
            List<IdentiteDto> dtoList = utilisateurService.getAll();
            System.err.println(dtoList.toString());
            return Response.ok(dtoList).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching users: " + e.getMessage())
                    .build();
        }
    }


    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(IdentiteDto utilisateurDto) {
        try {
            IdentiteDto createdUser = utilisateurService.register(utilisateurDto);
            return Response.status(Response.Status.CREATED).entity(createdUser).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error registering user: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    @AllowedRoles({"GERANT"})
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = utilisateurService.delete(id);
        if (deleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticate(IdentiteCredentialsRequest request) {
        try {
            LoginResponse response = utilisateurService.authentifier(request.getEmail(), request.getMotDePasse());
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Authentication failed: " + e.getMessage()).build();
        }
    }

    @PATCH
    @Path("/change-password")
    public Response changePassword(@QueryParam("id") Long id, PasswordRequest passwordRequest) {
        boolean result = utilisateurService.changerMotDePasse(id, passwordRequest.getAncienMotDePasse(), passwordRequest.getNouveauMotDePasse());
        if (result) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PATCH
    @Path("/{id}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AllowedRoles({"GERANT"})
    public Response updateStatus(@PathParam("id") Long id, StatusDto dto) {
        try {
            System.out.println("Updating status for user with ID: " + id + " to status: " + dto);
            IdentiteDto utilisateur = utilisateurService.changeStatus(id, dto.isActive());
            if (utilisateur == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }
            return Response.ok(utilisateur).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating user status: " + e.getMessage()).build();
        }
    }
}
