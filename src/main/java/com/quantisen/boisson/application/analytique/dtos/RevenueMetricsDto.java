package com.quantisen.boisson.application.analytique.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RevenueMetricsDto {
    private double totalRevenue;
    private double averageRevenuePerDay;
    private double highestDayRevenue;
    private String highestDay;
    private double lowestDayRevenue;
    private String lowestDay;
}