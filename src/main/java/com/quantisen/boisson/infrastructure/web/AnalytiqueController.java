package com.quantisen.boisson.infrastructure.web;

import com.quantisen.boisson.application.analytique.dtos.*;
import com.quantisen.boisson.application.analytique.services.AnalyticsService;
import com.quantisen.boisson.infrastructure.security.AllowedRoles;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@RequestScoped
@Path("/statistics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnalytiqueController {

    @Inject
    private AnalyticsService analyticsService;

    @GET
    @Path("/dashboard")
    public Response getDashboardStatistics() {
        DashboardStatisticsDto dashboardStatistics = analyticsService.getDashboardStatistics();
        return Response.ok(dashboardStatistics).build();
    }

    @GET
    @Path("/weekly-stock-movement")
    public Response getWeeklyStockMovement() {
        WeeklyStockMovementDto weeklyStockMovement = analyticsService.getWeeklyStockMovement();
        return Response.ok(weeklyStockMovement).build();
    }

    @GET
    @Path("/movement-trends")
    @AllowedRoles({"GERANT"})
    public Response getMovementTrends(@QueryParam("period") String period) {
        List<MovementTrendDto> movementTrends = analyticsService.getMovementTrends(period);
        return Response.ok(movementTrends).build();
    }

    @GET
    @Path("/daily-movements")
    @AllowedRoles({"GERANT"})
    public Response getDailyMovements(@QueryParam("start") String start, @QueryParam("end") String end) {
        DailyMovementsDto dailyMovements = analyticsService.getDailyMovements(start, end);
        return Response.ok(dailyMovements).build();
    }

    @GET
    @Path("/inventory-analytics")
    @AllowedRoles({"GERANT"})
    public Response getInventoryAnalytics() {
        InventoryAnalyticsDto analytics = analyticsService.getInventoryAnalytics();
        return Response.ok(analytics).build();
    }

    @GET
    @Path("/beverage-performance")
    @AllowedRoles({"GERANT"})
    public Response getBeveragePerformance() {
        List<TopBeverageDto> performance = analyticsService.getBeveragePerformance();
        return Response.ok(performance).build();
    }

    @GET
    @Path("/stock-alerts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStockAlerts() {
        return Response.ok(analyticsService.getStockAlerts()).build();
    }

    @GET
    @Path("/expiration-alerts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExpirationAlerts() {
        return Response.ok(analyticsService.getExpirationAlerts()).build();
    }

    @GET
    @Path("/user-activity")
    @Produces(MediaType.APPLICATION_JSON)
    @AllowedRoles({"GERANT"})
    public Response getUserActivity() {
        return Response.ok(analyticsService.getUserActivity()).build();
    }

    @GET
    @Path("/revenue-metrics")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRevenueMetrics() {
        return Response.ok(analyticsService.getRevenueMetrics()).build();
    }

    @GET
    @Path("/export/excel")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @AllowedRoles({"GERANT"})
    public Response exportDashboardExcel(@QueryParam("type") String type) {
        byte[] excel = analyticsService.exportDashboardExcel(type);
        return Response.ok(excel)
                .header("Content-Disposition", "attachment; filename=" + type + "_analytics.xlsx")
                .build();
    }

    @GET
    @Path("/export/pdf")
    @Produces("application/pdf")
    public Response exportDashboardPdf(@QueryParam("type") String type) {
        byte[] pdf = analyticsService.exportDashboardPdf(type);
        return Response.ok(pdf)
                .header("Content-Disposition", "attachment; filename=" + type + "_analytics.pdf")
                .build();
    }

    @GET
    @Path("/stocks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStocks() {
        List<StockAlertDto> stockAlerts = analyticsService.getStockAlerts();
        return Response.ok(stockAlerts).build();
    }
}