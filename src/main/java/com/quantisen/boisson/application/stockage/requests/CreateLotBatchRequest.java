package com.quantisen.boisson.application.stockage.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.quantisen.boisson.web.identite.dtos.IdentiteDto;
import com.quantisen.boisson.web.stockage.dtos.LotDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateLotBatchRequest {
    @JsonProperty("lots")
    private List<LotDto> lots;
    @JsonProperty("utilisateur")
    private IdentiteDto utilisateur;

}

