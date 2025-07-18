package com.quantisen.boisson.application.analytique.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardStatisticsDto {
    private int totalBeverages;
    private int totalStock;
    private int lowStockAlerts;
    private int totalMovements;
    private int totalUsers;
    private double totalValue;
    private List<StockAlertDto> stockAlerts;
}