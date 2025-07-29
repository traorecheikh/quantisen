package com.quantisen.boisson.application.fournisseur.mappers;

import com.quantisen.boisson.application.boisson.dtos.BoissonDto;
import com.quantisen.boisson.application.fournisseur.dtos.FournisseurDto;
import com.quantisen.boisson.domaine.boisson.domainModel.Boisson;
import com.quantisen.boisson.domaine.fournisseur.domainModel.Fournisseur;

public class FournisseurMapper {
    public static FournisseurDto toDto(Fournisseur fournisseur) {
        if (fournisseur == null) return null;
        return FournisseurDto.builder()
                .id(fournisseur.getId())
                .nom(fournisseur.getNom())
                .contact(fournisseur.getContact())
                .adresse(fournisseur.getAdresse())
                .statut(fournisseur.getStatut())
                .dateContrat(fournisseur.getDateContrat())
                .dateResiliation(fournisseur.getDateResiliation())
                .boissons(fournisseur.getBoissons() != null ?
                          fournisseur.getBoissons().stream()
                          .map(Boisson::toDto)
                          .toList() : null)
                .createdAt(fournisseur.getCreatedAt())
                .updatedAt(fournisseur.getUpdatedAt())
                .build();
    }

    public static Fournisseur toEntity(FournisseurDto dto) {
        if (dto == null) return null;
        return Fournisseur.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .contact(dto.getContact())
                .adresse(dto.getAdresse())
                .statut(dto.getStatut())
                .dateContrat(dto.getDateContrat())
                .dateResiliation(dto.getDateResiliation())
                .boissons(dto.getBoissons() != null ?
                          dto.getBoissons().stream()
                          .map(BoissonDto::toEntity)
                          .toList() : null)
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}

