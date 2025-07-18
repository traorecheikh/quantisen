package com.quantisen.boisson.application.analytique.services;

import com.quantisen.boisson.application.analytique.dtos.*;

import java.util.List;

public interface AnalyticsService {
    DashboardStatisticsDto getDashboardStatistics();

    WeeklyStockMovementDto getWeeklyStockMovement();

    List<MovementTrendDto> getMovementTrends(String period);

    DailyMovementsDto getDailyMovements(String start, String end);

    List<TopBeverageDto> getBeveragePerformance();

    InventoryAnalyticsDto getInventoryAnalytics();

    List<StockAlertDto> getStockAlerts();

    List<ExpirationAlertDto> getExpirationAlerts();

    List<AccountActivityDto> getUserActivity();

    RevenueMetricsDto getRevenueMetrics();

    byte[] exportDashboardExcel(String type);

    byte[] exportDashboardPdf(String type);
}