package com.quantisen.boisson.application.stockage.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.quantisen.boisson.application.identite.dtos.IdentiteDto;
import com.quantisen.boisson.application.stockage.dtos.LotDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateLotRequest {
    @JsonProperty("lot")
    private LotDto lot;
    @JsonProperty("utilisateur")
    private IdentiteDto utilisateur;

}

