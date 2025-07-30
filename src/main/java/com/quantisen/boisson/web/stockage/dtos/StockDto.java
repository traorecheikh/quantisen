package com.quantisen.boisson.web.stockage.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockDto {
    @JsonProperty("nom")
    private String nom;
    @JsonProperty("quantiteActuelle")
    private int quantiteActuelle;
    @JsonProperty("seuil")
    private String seuil;
    @JsonProperty("status")
    private String status;

}
