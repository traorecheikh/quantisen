package com.quantisen.boisson.application.analytique.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovementAnalysisDto {
    private List<TopBeverageDto> mostActiveBeverages;
    private List<TopBeverageDto> leastActiveBeverages;
    private List<MovementTrendDto> seasonalTrends;
}