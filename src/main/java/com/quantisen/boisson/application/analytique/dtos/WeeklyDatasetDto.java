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
public class WeeklyDatasetDto {
    private String label; // e.g. "Entries", "Exits", "Adjustments"
    private List<Integer> data;
    private String movementType; // e.g. "ENTREE", "SORTIE", "AJUSTEMENT"
    private String color; // e.g. "#10b981", "#ef4444", "#f59e0b"
}