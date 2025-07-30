package com.quantisen.boisson.application.stockage.mappers;

import com.quantisen.boisson.web.stockage.dtos.MouvementDto;
import com.quantisen.boisson.domaine.stockage.domainModel.Mouvement;

import java.util.List;

public interface MouvementMapper {
    Mouvement toEntity(MouvementDto dto);

    MouvementDto toDto(Mouvement entity);

    List<Mouvement> toEntityList(List<MouvementDto> dtoList);

    List<MouvementDto> toDtoList(List<Mouvement> entityList);
}
