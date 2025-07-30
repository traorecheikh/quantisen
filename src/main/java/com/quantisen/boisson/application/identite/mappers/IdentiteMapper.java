package com.quantisen.boisson.application.identite.mappers;

import com.quantisen.boisson.web.identite.dtos.IdentiteDto;
import com.quantisen.boisson.domaine.identite.domainModel.CompteUtilisateur;

import java.util.List;


public interface IdentiteMapper {
    IdentiteDto toDto(CompteUtilisateur utilisateur);

    CompteUtilisateur toEntity(IdentiteDto dto, boolean includePassword);

    List<IdentiteDto> toDtoList(List<CompteUtilisateur> utilisateurList);

    List<CompteUtilisateur> toEntityList(List<IdentiteDto> utilisateurDtos);

}

