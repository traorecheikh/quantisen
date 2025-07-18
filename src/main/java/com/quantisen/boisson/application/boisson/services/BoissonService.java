package com.quantisen.boisson.application.boisson.services;

import com.quantisen.boisson.application.boisson.dtos.BoissonDto;

import java.util.List;

public interface BoissonService {
    BoissonDto ajouterBoisson(BoissonDto dto);

    List<BoissonDto> ajouterBulkBoisson(List<BoissonDto> dtos);

    void supprimerBoisson(Long id);

    BoissonDto modifierBoisson(BoissonDto dto);

    BoissonDto getBoissonById(Long id);

    BoissonDto getBoissonByNom(String nom);

    List<BoissonDto> getAllBoisson();

    List<BoissonDto> getAllBoissonActive();

    List<BoissonDto> getAllBoissonInactive();

    void changeBoissonStatus(Long id);
}
