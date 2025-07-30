package com.quantisen.boisson.application.boisson.mappers.impl;

import com.quantisen.boisson.web.boisson.dtos.BoissonDto;
import com.quantisen.boisson.application.boisson.mappers.BoissonMapper;
import com.quantisen.boisson.domaine.boisson.domainModel.Boisson;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Named
public class BoissonMapperImpl implements BoissonMapper {
    @Override
    public Boisson toEntity(BoissonDto boissonDto) {
        if (boissonDto == null) return null;
        Boisson boisson = new Boisson();
        boisson.setId(boissonDto.getId());
        boisson.setNom(boissonDto.getNom());
        boisson.setDescription(boissonDto.getDescription());
        boisson.setActive(boissonDto.isActive());
        boisson.setPrix(boissonDto.getPrix());
        boisson.setSeuil(boissonDto.getSeuil());
        boisson.setVolume(boissonDto.getVolume());
        boisson.setUnite(boissonDto.getUnite());
        return boisson;
    }

    @Override
    public BoissonDto toDto(Boisson boisson) {
        if (boisson == null) return null;
        BoissonDto boissonDto = new BoissonDto();
        boissonDto.setId(boisson.getId());
        boissonDto.setNom(boisson.getNom());
        boissonDto.setDescription(boisson.getDescription());
        boissonDto.setActive(boisson.isActive());
        boissonDto.setPrix(boisson.getPrix());
        boissonDto.setSeuil(boisson.getSeuil());
        boissonDto.setVolume(boisson.getVolume());
        boissonDto.setUpdatedAt(boisson.getUpdatedAt());
        boissonDto.setCreatedAt(boisson.getCreatedAt());
        boissonDto.setUnite(boisson.getUnite());
        return boissonDto;
    }

    @Override
    public List<BoissonDto> toDtoList(List<Boisson> boissons) {
        if (boissons == null || boissons.isEmpty()) return Collections.emptyList();
        return boissons.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<Boisson> toEntityList(List<BoissonDto> boissonDtos) {
        if (boissonDtos == null || boissonDtos.isEmpty()) return Collections.emptyList();
        return boissonDtos.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
