package com.quantisen.boisson.application.stockage.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.quantisen.boisson.application.boisson.dtos.BoissonDto;
import com.quantisen.boisson.application.fournisseur.dtos.FournisseurDto;
import com.quantisen.boisson.domaine.stockage.domainModel.Lot;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class LotDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("numeroLot")
    private String numeroLot;

    @JsonProperty("quantiteInitiale")
    private int quantiteInitiale;

    @JsonProperty("quantiteActuelle")
    private int quantiteActuelle;

    @JsonProperty("mouvementEntree")
    private MouvementDto mouvementEntree;

    @JsonProperty("dateEntree")
    private String dateEntree;

    @JsonProperty("datePeremption")
    private String datePeremption;

    @JsonProperty("boisson")
    private BoissonDto boisson;

    @JsonProperty("vendable")
    private boolean vendable;

    @JsonProperty("fournisseurString")
    private String fournisseurString;

    @JsonProperty("fournisseur")
    private FournisseurDto fournisseur;

    public Lot toEntity() {
        return Lot.builder()
                .id(this.id)
                .numeroLot(this.numeroLot)
                .quantiteInitiale(this.quantiteInitiale)
                .quantiteActuelle(this.quantiteActuelle)
                .fournisseur(fournisseur == null ? null : fournisseur.toEntity())
                .mouvementEntree(mouvementEntree != null ? mouvementEntree.toEntity() : null)
                .dateEntree(this.dateEntree)
                .datePeremption(this.datePeremption)
                .boisson(boisson != null ? boisson.toEntity() : null)
                .vendable(this.vendable)
                .build();
    }
}
