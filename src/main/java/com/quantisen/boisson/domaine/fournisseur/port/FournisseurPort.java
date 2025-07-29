package com.quantisen.boisson.domaine.fournisseur.port;

import com.quantisen.boisson.domaine.fournisseur.domainModel.Fournisseur;
import java.util.List;

public interface FournisseurPort {
    List<Fournisseur> findAll();
    Fournisseur save(Fournisseur fournisseur);
    void deleteById(Long id);
    Fournisseur findById(Long id);
    List<Fournisseur> saveAll(List<Fournisseur> fournisseurs);
    Fournisseur findByNom(String nom);
}

