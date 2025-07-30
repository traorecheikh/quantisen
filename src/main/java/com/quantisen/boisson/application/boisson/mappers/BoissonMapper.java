package com.quantisen.boisson.application.boisson.mappers;

import com.quantisen.boisson.web.boisson.dtos.BoissonDto;
import com.quantisen.boisson.domaine.boisson.domainModel.Boisson;

import java.util.List;

public interface BoissonMapper {
    Boisson toEntity(BoissonDto boissonDto);

    BoissonDto toDto(Boisson boisson);

    List<BoissonDto> toDtoList(List<Boisson> boissons);

    List<Boisson> toEntityList(List<BoissonDto> boissonDtos);
}
