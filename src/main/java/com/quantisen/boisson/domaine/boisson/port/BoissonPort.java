package com.quantisen.boisson.domaine.boisson.port;

import com.quantisen.boisson.domaine.boisson.domainModel.Boisson;

import java.util.List;

public interface BoissonPort {
    List<Boisson> findAll();

    Boisson save(Boisson boisson);

    void deleteById(Long id);

    Boisson findById(Long id);

    List<Boisson> saveAll(List<Boisson> boissons);

    Boisson findByNom(String nom);

    List<Boisson> findLowStockItems();

    double getTotalStockValue();

    List<Boisson> findExpiredProducts();

    void changeBoissonStatus(Long id);
    int getTotalStockBoissonById(Long id);
}
