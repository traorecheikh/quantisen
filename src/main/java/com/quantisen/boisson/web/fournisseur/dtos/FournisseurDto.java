package com.quantisen.boisson.web.fournisseur.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.quantisen.boisson.domaine.stockage.domainModel.Lot;
import com.quantisen.boisson.web.boisson.dtos.BoissonDto;
import com.quantisen.boisson.domaine.fournisseur.domainModel.Fournisseur;
import com.quantisen.boisson.domaine.fournisseur.enums.StatutFournisseur;
import com.quantisen.boisson.web.stockage.dtos.LotDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FournisseurDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("nom")
    private String nom;
    @JsonProperty("contact")
    private String contact;
    @JsonProperty("adresse")
    private String adresse;
    @JsonProperty("statut")
    private StatutFournisseur statut;
    @JsonProperty("dateContrat")
    private String dateContrat;
    @JsonProperty("dateResiliation")
    private String dateResiliation;
    @JsonProperty("lots")
    private List<LotDto> lots;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("updatedAt")
    private String updatedAt;

    public Fournisseur toEntity() {
        return Fournisseur.builder()
                .id(this.id)
                .nom(this.nom)
                .contact(this.contact)
                .adresse(this.adresse)
                .lots(this.lots != null ? this.lots.stream()
                        .map(LotDto::toEntity)
                        .toList():List.of())
                .statut(this.statut)
                .dateContrat(this.dateContrat)
                .dateResiliation(this.dateResiliation)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
