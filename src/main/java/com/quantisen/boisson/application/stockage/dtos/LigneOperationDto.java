package com.quantisen.boisson.application.stockage.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.quantisen.boisson.domaine.stockage.domainModel.LigneOperation;
import com.quantisen.boisson.domaine.stockage.domainModel.Lot;
import com.quantisen.boisson.domaine.stockage.domainModel.Mouvement;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneOperationDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("mouvement")
    private MouvementDto mouvement;
    @JsonProperty("lot")
    private LotDto lot;
    @JsonProperty("quantite")
    private int quantite;

    public static List<LigneOperation> toEntityList(List<LigneOperationDto> ligneOperationDtos) {
        return ligneOperationDtos.stream()
                .map(LigneOperationDto::toEntity)
                .toList();
    }

    public LigneOperation toEntity() {
        Lot newLot = null;
        if (this.lot != null) {
            newLot = this.lot.toEntity();
        }
        Mouvement newM = null;
        if (this.mouvement != null) {
            newM = this.mouvement.toEntity();
        }
        LigneOperation ligneOperation = LigneOperation.builder()
                .id(this.id)
                .lot(newLot)
                .mouvement(newM)
                .quantite(this.quantite)
                .build();
        if (newLot != null) {
            newLot.getLigneOperations().add(ligneOperation);
        }
        return ligneOperation;
    }
}
