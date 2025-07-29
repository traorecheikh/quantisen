package com.quantisen.boisson.application.fournisseur.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.quantisen.boisson.application.boisson.dtos.BoissonDto;
import com.quantisen.boisson.domaine.fournisseur.domainModel.Fournisseur;
import com.quantisen.boisson.domaine.fournisseur.enums.StatutFournisseur;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    @JsonProperty("boissons")
    private List<BoissonDto> boissons;
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
                .statut(this.statut)
                .dateContrat(this.dateContrat)
                .dateResiliation(this.dateResiliation)
                .boissons(boissons != null ? boissons.stream().map(BoissonDto::toEntity).toList() : null)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
