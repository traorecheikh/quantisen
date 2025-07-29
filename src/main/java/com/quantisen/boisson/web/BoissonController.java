package com.quantisen.boisson.web;

import com.quantisen.boisson.application.boisson.dtos.BoissonDto;
import com.quantisen.boisson.application.boisson.services.BoissonService;
import com.quantisen.boisson.infrastructure.security.AllowedRoles;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@RequestScoped
@Path("/boissons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoissonController {
    @Inject
    private BoissonService service;


    @PATCH
    @Path("/status/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @AllowedRoles({"GERANT"})
    public Response changeBoisson(@PathParam("id") Long id){
        try{
            service.changeBoissonStatus(id);
            return Response.ok().build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @AllowedRoles({"GERANT"})
    public Response addBoisson(BoissonDto dto) {
        try {
            BoissonDto boisson = service.ajouterBoisson(dto);
            return Response.status(Response.Status.CREATED).entity(boisson).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @AllowedRoles({"GERANT"})
    public Response updateBoisson(@QueryParam("id") Long id, BoissonDto dto) {
        try {
            BoissonDto boisson = service.modifierBoisson(dto);
            return Response.status(Response.Status.CREATED).entity(boisson).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllBoissons() {
        List<BoissonDto> boissonDtos = service.getAllBoisson();
        try {
            return Response.ok(boissonDtos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
