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
public class WeeklyStockMovementDto {
    private List<String> weekDates;
    private List<WeeklyDatasetDto> datasets;
    private int totalEntries;
    private int totalExits;
    private int totalAdjustments;
}