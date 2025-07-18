package com.quantisen.boisson.infrastructure.web;

import com.quantisen.boisson.application.stockage.dtos.LigneOperationDto;
import com.quantisen.boisson.application.stockage.dtos.LotDto;
import com.quantisen.boisson.application.stockage.dtos.MouvementDto;
import com.quantisen.boisson.application.stockage.requests.CreateLotBatchRequest;
import com.quantisen.boisson.application.stockage.requests.CreateLotRequest;
import com.quantisen.boisson.application.stockage.requests.CreateMouvementSortieRequest;
import com.quantisen.boisson.application.stockage.requests.CreateMouvementAjustementRequest;
import com.quantisen.boisson.application.stockage.services.StockageService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@RequestScoped
@Path("/inventaire")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StockageController {

    @Inject
    private StockageService stockageService;

    @POST
    @Path(("/entree"))
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createLot(CreateLotRequest request) {
        try {
            System.out.println("ddd");
            LigneOperationDto created = stockageService.entreeSimple(request.getLot(), request.getUtilisateur());
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("/batch")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createLotBatch(CreateLotBatchRequest request) {
        try {
            System.out.println("ddd");
            List<LigneOperationDto> created = stockageService.entreeBatch(request.getLots(), request.getUtilisateur());
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("/sortie")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMouvementSortie(CreateMouvementSortieRequest request) {
        try {
            stockageService.sortie(request.getBoissonId(), request.getQuantiteDemandee(), request.getUtilisateur());
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("/ajustement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAjustement(CreateMouvementAjustementRequest request) {
        try {
            stockageService.ajustement(request.getLotId(), request.getDelta(), request.getRaison(), request.getUtilisateur());
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/lots")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllLots() {
        try {
            List<LotDto> lots = stockageService.getAllLots();
            return Response.ok(lots).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/lots/valid")
    public Response getAllValidLots() {
        try {
            List<LotDto> lots = stockageService.getAllValidLots();
            return Response.ok(lots).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/lots/boisson/{boissonId}")
    public Response getAllLotsByBoissonId(@PathParam("boissonId") Long boissonId) {
        try {
            List<LotDto> lots = stockageService.getAllLotsByBoissonId(boissonId);
            return Response.ok(lots).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/mouvements")
    public Response getAllMouvements() {
        try {
            List<MouvementDto> mouvements = stockageService.getAllMouvements();
            return Response.ok(mouvements).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/mouvements/boisson/{boissonId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllMouvementsByBoissonId(@PathParam("boissonId") Long boissonId) {
        try {
            List<MouvementDto> mouvements = stockageService.getAllMouvementsByBoissonId(boissonId);
            return Response.ok(mouvements).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/mouvements/lot/{lotId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllMouvementsByLotId(@PathParam("lotId") Long lotId) {
        try {
            List<MouvementDto> mouvements = stockageService.getAllMouvementsByLotId(lotId);
            return Response.ok(mouvements).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/ligne-operations/mouvement/{mouvementId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllLigneOperationsByMouvementId(@PathParam("mouvementId") Long mouvementId) {
        try {
            List<LigneOperationDto> ligneOperations = stockageService.getAllLigneOperationsByMouvementId(mouvementId);
            return Response.ok(ligneOperations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/ligne-operations")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllLigneOperations(@QueryParam("page") @DefaultValue("1") int page,
                                          @QueryParam("size") @DefaultValue("10") int size) {
        try {
            List<LigneOperationDto> ligneOperations = stockageService.getLigneOperationsPaginated(page, size);
            long total = stockageService.countLigneOperations();
            return Response.ok(new PaginatedResponse<>(ligneOperations, page, size, total)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/ligne-operations/lot/{lotId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllLigneOperationsByLotId(@PathParam("lotId") Long lotId) {
        try {
            List<LigneOperationDto> ligneOperations = stockageService.getAllLigneOperationsByLotId(lotId);
            return Response.ok(ligneOperations).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/utilisateur/{utilisateurId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllMouvementsByUtilisateurId(@PathParam("utilisateurId") Long utilisateurId) {
        try {
            List<MouvementDto> mouvements = stockageService.getAllMouvementsByUtilisateur(utilisateurId);
            return Response.ok(mouvements).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    public static class PaginatedResponse<T> {
        public List<T> data;
        public int page;
        public int size;
        public long total;

        public PaginatedResponse(List<T> data, int page, int size, long total) {
            this.data = data;
            this.page = page;
            this.size = size;
            this.total = total;
        }
    }
}
