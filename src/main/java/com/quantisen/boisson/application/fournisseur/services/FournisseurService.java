package com.quantisen.boisson.application.fournisseur.services;

import com.quantisen.boisson.application.fournisseur.dtos.FournisseurDto;

import java.util.List;

public interface FournisseurService {

    List<FournisseurDto> recupererTousLesFournisseurs();

    FournisseurDto enregistrerNouveauFournisseur(FournisseurDto fournisseurDto);

    void supprimerFournisseur(Long id);

    FournisseurDto rechercherFournisseurParId(Long id);

    FournisseurDto rechercherFournisseurParNom(String nom);

    void validerContratFournisseur(Long id);

    void resilierContratFournisseur(Long id);

    void suspendreFournisseur(Long id);

    void reactiverFournisseur(Long id);
}
