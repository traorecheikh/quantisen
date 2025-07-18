package com.quantisen.boisson.application.analytique.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecentMovementDto {
    private String beverageName;
    private String movementType; // ENTREE, SORTIE, AJUSTEMENT
    private int quantityMoved;
    private String dateTime;
    private String user;
}