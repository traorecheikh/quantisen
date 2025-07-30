package com.quantisen.boisson.application.stockage.mappers;

import com.quantisen.boisson.web.stockage.dtos.LotDto;
import com.quantisen.boisson.domaine.stockage.domainModel.Lot;

import java.util.List;

public interface LotMapper {
    Lot toEntity(LotDto dto);

    LotDto toDto(Lot entity);

    List<Lot> toEntityList(List<LotDto> dtoList);

    List<LotDto> toDtoList(List<Lot> entityList);
}
