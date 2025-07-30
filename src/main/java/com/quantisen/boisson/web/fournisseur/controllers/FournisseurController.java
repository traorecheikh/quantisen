package com.quantisen.boisson.web.fournisseur.controllers;

import com.quantisen.boisson.web.fournisseur.dtos.FournisseurDto;
import com.quantisen.boisson.application.fournisseur.services.FournisseurService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;


@RequestScoped
@Path("/fournisseurs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FournisseurController {
    @Inject
    private FournisseurService fournisseurService;

    @GET
    public List<FournisseurDto> getAll() {
        return fournisseurService.recupererTousLesFournisseurs();
    }

    @GET
    @Path("/{id}")
    public FournisseurDto getById(@PathParam("id") Long id) {
        return fournisseurService.rechercherFournisseurParId(id);
    }

    @POST
    public FournisseurDto create(FournisseurDto dto) {
        return fournisseurService.enregistrerNouveauFournisseur(dto);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        fournisseurService.supprimerFournisseur(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}/valider")
    public Response validerContrat(@PathParam("id") Long id) {
        fournisseurService.validerContratFournisseur(id);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}/resilier")
    public Response resilierContrat(@PathParam("id") Long id) {
        fournisseurService.resilierContratFournisseur(id);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}/suspendre")
    public Response suspendre(@PathParam("id") Long id) {
        fournisseurService.suspendreFournisseur(id);
        return Response.ok().build();
    }


    @PUT
    @Path("/{id}/reactiver")
    public Response reactiver(@PathParam("id") Long id) {
        fournisseurService.reactiverFournisseur(id);
        return Response.ok().build();
    }
}

