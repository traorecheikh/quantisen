package com.quantisen.boisson.domaine.stockage.port;

import com.quantisen.boisson.domaine.stockage.domainModel.Lot;

import java.util.List;
import java.util.Optional;

public interface LotPort {
    Lot save(Lot nouveauLot);

    List<Lot> saveAll(List<Lot> lots);


    void update(Lot lot);

    Optional<Lot> findById(Long id);

    List<Lot> findValidLotsByBoissonId(Long boissonId);

    List<Lot> findAllValidLots();

    List<Lot> findAllLotsByBoissonId(Long boissonId);

    List<Lot> findAll();

    int countAllByBoissonId(Long id);
}