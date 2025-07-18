package com.quantisen.boisson.application.stockage.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.quantisen.boisson.application.identite.dtos.IdentiteDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateMouvementAjustementRequest {
    @JsonProperty("lotId")
    private Long lotId;
    @JsonProperty("delta")
    private int delta;
    @JsonProperty("raison")
    private String raison;
    @JsonProperty("utilisateur")
    private IdentiteDto utilisateur;
}

