package com.quantisen.boisson.application.fournisseur.mappers;

import com.quantisen.boisson.domaine.stockage.domainModel.Lot;
import com.quantisen.boisson.web.boisson.dtos.BoissonDto;
import com.quantisen.boisson.web.fournisseur.dtos.FournisseurDto;
import com.quantisen.boisson.domaine.boisson.domainModel.Boisson;
import com.quantisen.boisson.domaine.fournisseur.domainModel.Fournisseur;
import com.quantisen.boisson.web.stockage.dtos.LotDto;

import java.util.List;

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
                .createdAt(fournisseur.getCreatedAt())
                .updatedAt(fournisseur.getUpdatedAt())
                .lots(fournisseur.getLots() != null ? fournisseur.getLots().stream()
                        .map(Lot::toDto)
                        .toList() : List.of())
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
                .lots(dto.getLots() != null ? dto.getLots().stream()
                        .map(LotDto::toEntity)
                        .toList() : List.of())
                .dateContrat(dto.getDateContrat())
                .dateResiliation(dto.getDateResiliation())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}

