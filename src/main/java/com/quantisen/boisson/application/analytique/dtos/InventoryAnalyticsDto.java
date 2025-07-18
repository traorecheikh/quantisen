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
public class InventoryAnalyticsDto {
    private List<StockDistributionDto> stockDistribution;
    private ExpirationTrackingDto expirationTracking;  // Changed from List to single object
    private MovementAnalysisDto movementAnalysis;      // Changed from List to single object
}