package com.quantisen.boisson.web.boisson.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.quantisen.boisson.domaine.boisson.domainModel.Boisson;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class BoissonDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("nom")
    private String nom;
    @JsonProperty("description")
    private String description;
    @JsonProperty("prix")
    private double prix;
    @JsonProperty("volume")
    private double volume;
    @JsonProperty("unite")
    private String unite;
    @JsonProperty("seuil")
    private int seuil;
    @JsonProperty("isActive")
    private boolean isActive;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("updatedAt")
    private String updatedAt;

    public Boisson toEntity() {
        return Boisson.builder()
                .id(this.id)
                .nom(this.nom)
                .description(this.description)
                .prix(this.prix)
                .volume(this.volume)
                .unite(this.unite)
                .seuil(this.seuil)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
