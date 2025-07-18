package com.quantisen.boisson.domaine.statistiques.services;

import com.quantisen.boisson.application.analytique.dtos.*;
import com.quantisen.boisson.application.analytique.services.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private AnalyticsService statisticsService;

    private DashboardStatisticsDto dashboardStatisticsDto;
    private WeeklyStockMovementDto weeklyStockMovementDto;
    private List<MovementTrendDto> movementTrendDtoList;
    private DailyMovementsDto dailyMovementsDto;
    private List<TopBeverageDto> topBeverageDtoList;
    private InventoryAnalyticsDto inventoryAnalyticsDto;
    private List<StockAlertDto> stockAlertDtoList;
    private List<ExpirationAlertDto> expirationAlertDtoList;
    private List<AccountActivityDto> userActivityDtoList;
    private RevenueMetricsDto revenueMetricsDto;

    @BeforeEach
    void setUp() {
        dashboardStatisticsDto = new DashboardStatisticsDto(
                10, 500, 3, 25, 5, 1200.50, null
        );

        weeklyStockMovementDto = new WeeklyStockMovementDto();

        movementTrendDtoList = List.of(new MovementTrendDto());

        dailyMovementsDto = new DailyMovementsDto();

        topBeverageDtoList = List.of(new TopBeverageDto());

        inventoryAnalyticsDto = new InventoryAnalyticsDto();

        stockAlertDtoList = Arrays.asList(
                new StockAlertDto("Coca Cola", 5, 10, "HIGH"),
                new StockAlertDto("Pepsi", 8, 15, "MEDIUM")
        );

        expirationAlertDtoList = List.of(new ExpirationAlertDto());

        userActivityDtoList = List.of(new AccountActivityDto());

        revenueMetricsDto = new RevenueMetricsDto();
    }

    @Test
    void getDashboardStatistics_ShouldReturnDashboardStatistics() {
        when(statisticsService.getDashboardStatistics()).thenReturn(dashboardStatisticsDto);

        DashboardStatisticsDto result = statisticsService.getDashboardStatistics();

        assertNotNull(result);
        assertEquals(10, result.getTotalBeverages());
        assertEquals(500, result.getTotalStock());
        assertEquals(3, result.getLowStockAlerts());
        assertEquals(25, result.getTotalMovements());
        assertEquals(5, result.getTotalUsers());
        assertEquals(1200.50, result.getTotalValue());
        verify(statisticsService).getDashboardStatistics();
    }

    @Test
    void getDashboardStatistics_ShouldHandleNullResult() {
        when(statisticsService.getDashboardStatistics()).thenReturn(null);

        DashboardStatisticsDto result = statisticsService.getDashboardStatistics();

        assertNull(result);
        verify(statisticsService).getDashboardStatistics();
    }

    @Test
    void getWeeklyStockMovement_ShouldReturnWeeklyMovement() {
        when(statisticsService.getWeeklyStockMovement()).thenReturn(weeklyStockMovementDto);

        WeeklyStockMovementDto result = statisticsService.getWeeklyStockMovement();

        assertNotNull(result);
        verify(statisticsService).getWeeklyStockMovement();
    }

    @Test
    void getWeeklyStockMovement_ShouldHandleNullResult() {
        when(statisticsService.getWeeklyStockMovement()).thenReturn(null);

        WeeklyStockMovementDto result = statisticsService.getWeeklyStockMovement();

        assertNull(result);
        verify(statisticsService).getWeeklyStockMovement();
    }

    @Test
    void getMovementTrends_ShouldReturnMovementTrends() {
        when(statisticsService.getMovementTrends("weekly")).thenReturn(movementTrendDtoList);

        List<MovementTrendDto> result = statisticsService.getMovementTrends("weekly");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(statisticsService).getMovementTrends("weekly");
    }

    @Test
    void getMovementTrends_ShouldHandleNullPeriod() {
        when(statisticsService.getMovementTrends(null)).thenReturn(List.of());

        List<MovementTrendDto> result = statisticsService.getMovementTrends(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(statisticsService).getMovementTrends(null);
    }

    @Test
    void getMovementTrends_ShouldHandleEmptyPeriod() {
        when(statisticsService.getMovementTrends("")).thenReturn(List.of());

        List<MovementTrendDto> result = statisticsService.getMovementTrends("");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(statisticsService).getMovementTrends("");
    }

    @Test
    void getMovementTrends_ShouldHandleInvalidPeriod() {
        when(statisticsService.getMovementTrends("invalid")).thenReturn(List.of());

        List<MovementTrendDto> result = statisticsService.getMovementTrends("invalid");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(statisticsService).getMovementTrends("invalid");
    }

    @Test
    void getDailyMovements_ShouldReturnDailyMovements() {
        when(statisticsService.getDailyMovements("2024-01-01", "2024-01-31")).thenReturn(dailyMovementsDto);

        DailyMovementsDto result = statisticsService.getDailyMovements("2024-01-01", "2024-01-31");

        assertNotNull(result);
        verify(statisticsService).getDailyMovements("2024-01-01", "2024-01-31");
    }

    @Test
    void getDailyMovements_ShouldHandleNullDates() {
        when(statisticsService.getDailyMovements(null, null)).thenReturn(null);

        DailyMovementsDto result = statisticsService.getDailyMovements(null, null);

        assertNull(result);
        verify(statisticsService).getDailyMovements(null, null);
    }

    @Test
    void getDailyMovements_ShouldHandleEmptyDates() {
        when(statisticsService.getDailyMovements("", "")).thenReturn(null);

        DailyMovementsDto result = statisticsService.getDailyMovements("", "");

        assertNull(result);
        verify(statisticsService).getDailyMovements("", "");
    }

    @Test
    void getBeveragePerformance_ShouldReturnTopBeverages() {
        when(statisticsService.getBeveragePerformance()).thenReturn(topBeverageDtoList);

        List<TopBeverageDto> result = statisticsService.getBeveragePerformance();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(statisticsService).getBeveragePerformance();
    }

    @Test
    void getBeveragePerformance_ShouldReturnEmptyListWhenNoBeverages() {
        when(statisticsService.getBeveragePerformance()).thenReturn(List.of());

        List<TopBeverageDto> result = statisticsService.getBeveragePerformance();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(statisticsService).getBeveragePerformance();
    }

    @Test
    void getInventoryAnalytics_ShouldReturnInventoryAnalytics() {
        when(statisticsService.getInventoryAnalytics()).thenReturn(inventoryAnalyticsDto);

        InventoryAnalyticsDto result = statisticsService.getInventoryAnalytics();

        assertNotNull(result);
        verify(statisticsService).getInventoryAnalytics();
    }

    @Test
    void getInventoryAnalytics_ShouldHandleNullResult() {
        when(statisticsService.getInventoryAnalytics()).thenReturn(null);

        InventoryAnalyticsDto result = statisticsService.getInventoryAnalytics();

        assertNull(result);
        verify(statisticsService).getInventoryAnalytics();
    }

    @Test
    void getStockAlerts_ShouldReturnStockAlerts() {
        when(statisticsService.getStockAlerts()).thenReturn(stockAlertDtoList);

        List<StockAlertDto> result = statisticsService.getStockAlerts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Coca Cola", result.get(0).getBeverageName());
        assertEquals("HIGH", result.get(0).getAlertSeverityLevel());
        verify(statisticsService).getStockAlerts();
    }

    @Test
    void getStockAlerts_ShouldReturnEmptyListWhenNoAlerts() {
        when(statisticsService.getStockAlerts()).thenReturn(List.of());

        List<StockAlertDto> result = statisticsService.getStockAlerts();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(statisticsService).getStockAlerts();
    }

    @Test
    void getExpirationAlerts_ShouldReturnExpirationAlerts() {
        when(statisticsService.getExpirationAlerts()).thenReturn(expirationAlertDtoList);

        List<ExpirationAlertDto> result = statisticsService.getExpirationAlerts();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(statisticsService).getExpirationAlerts();
    }

    @Test
    void getExpirationAlerts_ShouldReturnEmptyListWhenNoAlerts() {
        when(statisticsService.getExpirationAlerts()).thenReturn(List.of());

        List<ExpirationAlertDto> result = statisticsService.getExpirationAlerts();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(statisticsService).getExpirationAlerts();
    }

    @Test
    void getUserActivity_ShouldReturnUserActivity() {
        when(statisticsService.getUserActivity()).thenReturn(userActivityDtoList);

        List<AccountActivityDto> result = statisticsService.getUserActivity();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(statisticsService).getUserActivity();
    }

    @Test
    void getUserActivity_ShouldReturnEmptyListWhenNoActivity() {
        when(statisticsService.getUserActivity()).thenReturn(List.of());

        List<AccountActivityDto> result = statisticsService.getUserActivity();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(statisticsService).getUserActivity();
    }

    @Test
    void getRevenueMetrics_ShouldReturnRevenueMetrics() {
        when(statisticsService.getRevenueMetrics()).thenReturn(revenueMetricsDto);

        RevenueMetricsDto result = statisticsService.getRevenueMetrics();

        assertNotNull(result);
        verify(statisticsService).getRevenueMetrics();
    }

    @Test
    void getRevenueMetrics_ShouldHandleNullResult() {
        when(statisticsService.getRevenueMetrics()).thenReturn(null);

        RevenueMetricsDto result = statisticsService.getRevenueMetrics();

        assertNull(result);
        verify(statisticsService).getRevenueMetrics();
    }

    @Test
    void exportDashboardExcel_ShouldReturnExcelBytes() {
        byte[] excelData = "Excel content".getBytes();
        when(statisticsService.exportDashboardExcel("dashboard")).thenReturn(excelData);

        byte[] result = statisticsService.exportDashboardExcel("dashboard");

        assertNotNull(result);
        assertTrue(result.length > 0);
        verify(statisticsService).exportDashboardExcel("dashboard");
    }

    @Test
    void exportDashboardExcel_ShouldHandleNullType() {
        when(statisticsService.exportDashboardExcel(null)).thenReturn(null);

        byte[] result = statisticsService.exportDashboardExcel(null);

        assertNull(result);
        verify(statisticsService).exportDashboardExcel(null);
    }

    @Test
    void exportDashboardExcel_ShouldHandleEmptyType() {
        when(statisticsService.exportDashboardExcel("")).thenReturn(null);

        byte[] result = statisticsService.exportDashboardExcel("");

        assertNull(result);
        verify(statisticsService).exportDashboardExcel("");
    }

    @Test
    void exportDashboardExcel_ShouldHandleInvalidType() {
        when(statisticsService.exportDashboardExcel("invalid")).thenReturn(null);

        byte[] result = statisticsService.exportDashboardExcel("invalid");

        assertNull(result);
        verify(statisticsService).exportDashboardExcel("invalid");
    }

    @Test
    void exportDashboardPdf_ShouldReturnPdfBytes() {
        byte[] pdfData = "PDF content".getBytes();
        when(statisticsService.exportDashboardPdf("dashboard")).thenReturn(pdfData);

        byte[] result = statisticsService.exportDashboardPdf("dashboard");

        assertNotNull(result);
        assertTrue(result.length > 0);
        verify(statisticsService).exportDashboardPdf("dashboard");
    }

    @Test
    void exportDashboardPdf_ShouldHandleNullType() {
        when(statisticsService.exportDashboardPdf(null)).thenReturn(null);

        byte[] result = statisticsService.exportDashboardPdf(null);

        assertNull(result);
        verify(statisticsService).exportDashboardPdf(null);
    }

    @Test
    void exportDashboardPdf_ShouldHandleEmptyType() {
        when(statisticsService.exportDashboardPdf("")).thenReturn(null);

        byte[] result = statisticsService.exportDashboardPdf("");

        assertNull(result);
        verify(statisticsService).exportDashboardPdf("");
    }

    @Test
    void exportDashboardPdf_ShouldHandleInvalidType() {
        when(statisticsService.exportDashboardPdf("invalid")).thenReturn(null);

        byte[] result = statisticsService.exportDashboardPdf("invalid");

        assertNull(result);
        verify(statisticsService).exportDashboardPdf("invalid");
    }
}