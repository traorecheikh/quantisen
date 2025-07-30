package com.quantisen.boisson.application.stockage.mappers;

import com.quantisen.boisson.web.stockage.dtos.LigneOperationDto;
import com.quantisen.boisson.domaine.stockage.domainModel.LigneOperation;

import java.util.List;

public interface LigneOperationMapper {
    LigneOperation toEntity(LigneOperationDto dto);

    LigneOperationDto toDto(LigneOperation entity);

    List<LigneOperation> toEntityList(List<LigneOperationDto> dtoList);

    List<LigneOperationDto> toDtoList(List<LigneOperation> entityList);
}
