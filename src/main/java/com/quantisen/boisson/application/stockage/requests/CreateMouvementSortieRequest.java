package com.quantisen.boisson.application.stockage.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.quantisen.boisson.application.identite.dtos.IdentiteDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateMouvementSortieRequest {
    @JsonProperty("boissonId")
    private Long boissonId;
    @JsonProperty("quantiteDemandee")
    private int quantiteDemandee;
    @JsonProperty("utilisateur")
    private IdentiteDto utilisateur;
}

