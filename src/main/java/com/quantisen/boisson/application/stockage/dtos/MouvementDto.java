package com.quantisen.boisson.application.stockage.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.quantisen.boisson.application.identite.dtos.IdentiteDto;
import com.quantisen.boisson.domaine.stockage.domainModel.LigneOperation;
import com.quantisen.boisson.domaine.stockage.domainModel.Mouvement;
import com.quantisen.boisson.domaine.stockage.enums.TypeAjustement;
import com.quantisen.boisson.domaine.stockage.enums.TypeMouvement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class MouvementDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("type")
    private TypeMouvement type;
    @JsonProperty("dateMouvement")
    private String dateMouvement;
    @JsonProperty("quantite")
    private int quantite;
    @JsonProperty("utilisateur")
    private IdentiteDto utilisateur;
    @JsonProperty("ligneOperations")
    private List<LigneOperationDto> ligneOperation;
    @JsonProperty("typeAjustement")
    private TypeAjustement typeAjustement;
    @JsonProperty("raison")
    private String raison;


    public Mouvement toEntity() {
        Mouvement mouvement = Mouvement.builder()
                .id(this.id)
                .type(this.type)
                .dateMouvement(this.dateMouvement)
                .quantite(this.quantite)
                .utilisateur(this.utilisateur.toEntity(false))
                .typeAjustement(this.typeAjustement)
                .raison(this.raison)
                .build();
        if (this.ligneOperation != null) {
            this.ligneOperation.forEach(ligneDto -> {
                LigneOperation ligneOp = ligneDto.toEntity();
                ligneOp.setMouvement(mouvement);
                mouvement.getLigneOperations().add(ligneOp);
            });
        }
        return mouvement;
    }

}