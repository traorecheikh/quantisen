package com.quantisen.boisson.application.analytique.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovementTrendDto {
    private String period; // e.g. "Cette Semaine", "Ce Mois", "Cette Année"
    private int totalMovements;
    private String trend; // "UP", "DOWN", "STABLE"
}