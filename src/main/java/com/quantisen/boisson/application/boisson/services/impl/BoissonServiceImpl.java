package com.quantisen.boisson.application.boisson.services.impl;

import com.quantisen.boisson.application.boisson.dtos.BoissonDto;
import com.quantisen.boisson.application.boisson.mappers.BoissonMapper;
import com.quantisen.boisson.application.boisson.services.BoissonService;
import com.quantisen.boisson.domaine.boisson.domainModel.Boisson;
import com.quantisen.boisson.domaine.boisson.port.BoissonPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@ApplicationScoped
@Named
@Default
public class BoissonServiceImpl implements BoissonService {
    @Inject
    private BoissonPort repository;
    @Inject
    private BoissonMapper mapper;
//    @Inject
//    private StockDao stockDao;

    @Override
    public BoissonDto ajouterBoisson(BoissonDto dto) {
        Boisson boissonExist = null;
        try {
            boissonExist = repository.findByNom(dto.getNom());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche de la boisson: " + e.getMessage(), e);
        }
        if (boissonExist != null) {
            throw new RuntimeException("boisson existe deja");
        }
        Boisson boisson = mapper.toEntity(dto);
        Boisson createdBoisson = repository.save(boisson);

        if (createdBoisson.getId() == null) {
            throw new RuntimeException("Failed to save Boisson: ID is null.");
        }

//        Stock stock = new Stock();
//        stock.setBoisson(createdBoisson);
//        stock.setQuantiteTotale(0);
//        stockDao.save(stock);

        return dto;
    }

    @Override
    public List<BoissonDto> ajouterBulkBoisson(List<BoissonDto> dtos) {
        List<Boisson> boissons = mapper.toEntityList(dtos);
        List<Boisson> createdBoissons = repository.saveAll(boissons);
        if (createdBoissons.isEmpty()) {
            throw new RuntimeException("Failed to save Boissons: No records created.");
        }
        return mapper.toDtoList(createdBoissons);
    }

    @Override
    public void supprimerBoisson(Long id) {
        Boisson boisson = repository.findById(id);
        if (boisson == null) {
            throw new RuntimeException("la boisson n'existe pas ");
        }
        repository.deleteById(id);
    }

    @Override
    public BoissonDto modifierBoisson(BoissonDto dto) {
        Boisson boissonExist = repository.findByNom(dto.getNom());
        if (boissonExist == null) {
            throw new RuntimeException("la boisson n'existe pas");
        }
        Boisson boisson = mapper.toEntity(dto);
        repository.save(boisson);
        return dto;
    }

    @Override
    public BoissonDto getBoissonById(Long id) {
        return null;
    }

    @Override
    public BoissonDto getBoissonByNom(String nom) {
        Boisson boisson = repository.findByNom(nom);
        if (boisson == null) {
            return null;
        }
        return mapper.toDto(boisson);
    }

    @Override
    public List<BoissonDto> getAllBoisson() {
        List<Boisson> boissons = repository.findAll();
        System.out.println("Boissons: " + boissons);
        return mapper.toDtoList(boissons);
    }

    @Override
    public List<BoissonDto> getAllBoissonActive() {
        List<Boisson> boissons = repository.findAll()
                .stream()
                .filter(Boisson::isActive)
                .toList();
        return mapper.toDtoList(boissons);

    }

    @Override
    public List<BoissonDto> getAllBoissonInactive() {
        List<Boisson> boissons = repository.findAll()
                .stream()
                .filter(b -> !b.isActive())
                .toList();
        return mapper.toDtoList(boissons);
    }

    @Override
    public void changeBoissonStatus(Long id) {
        try{
            repository.changeBoissonStatus(id);
        }catch (Exception e){
            throw e;
        }
    }
}

