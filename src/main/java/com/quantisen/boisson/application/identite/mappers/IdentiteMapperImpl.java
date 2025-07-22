package com.quantisen.boisson.application.identite.mappers;

import com.quantisen.boisson.application.identite.dtos.IdentiteDto;
import com.quantisen.boisson.domaine.identite.domainModel.CompteUtilisateur;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.List;

@ApplicationScoped
@Named
public class IdentiteMapperImpl implements IdentiteMapper {
    public IdentiteDto toDto(CompteUtilisateur utilisateur) {
        if (utilisateur == null) return null;
        IdentiteDto dto = new IdentiteDto();
        dto.setId(utilisateur.getId());
        dto.setEmail(utilisateur.getEmail());
        dto.setRole(utilisateur.getRole());
        dto.setFirstLogin(utilisateur.isFirstLogin());
        dto.setCreatedAt(utilisateur.getCreatedAt());
        dto.setUpdatedAt(utilisateur.getUpdatedAt());
        dto.setActive(utilisateur.isActive());
        return dto;
    }

    public CompteUtilisateur toEntity(IdentiteDto dto) {
        return toEntity(dto, false);
    }

    @Override
    public CompteUtilisateur toEntity(IdentiteDto dto, boolean includePassword) {
        if (dto == null) return null;

        CompteUtilisateur utilisateur = new CompteUtilisateur();
        utilisateur.setId(dto.getId());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setRole(dto.getRole());
        utilisateur.setActive(dto.isActive());
        utilisateur.setFirstLogin(dto.isFirstLogin());
        utilisateur.setCreatedAt(dto.getCreatedAt());
        utilisateur.setUpdatedAt(dto.getUpdatedAt());

        if (includePassword) {
            utilisateur.setMotDePasse(dto.getMotDePasse());
        }

        return utilisateur;
    }


    @Override
    public List<IdentiteDto> toDtoList(List<CompteUtilisateur> utilisateurList) {
        if (utilisateurList == null || utilisateurList.isEmpty()) return List.of();
        return utilisateurList.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<CompteUtilisateur> toEntityList(List<IdentiteDto> utilisateurDtos) {
        if (utilisateurDtos == null || utilisateurDtos.isEmpty()) return List.of();
        return utilisateurDtos.stream()
                .map(this::toEntity)
                .toList();
    }
}
