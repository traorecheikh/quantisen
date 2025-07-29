package com.quantisen.boisson.application.fournisseur.services;

import com.quantisen.boisson.application.fournisseur.dtos.FournisseurDto;
import com.quantisen.boisson.application.fournisseur.mappers.FournisseurMapper;
import com.quantisen.boisson.domaine.fournisseur.domainModel.Fournisseur;
import com.quantisen.boisson.domaine.fournisseur.enums.StatutFournisseur;
import com.quantisen.boisson.domaine.fournisseur.port.FournisseurPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@ApplicationScoped
@Named
public class FournisseurServiceImpl implements FournisseurService {
    @Inject
    private FournisseurPort fournisseurPort;

    @Override
    public List<FournisseurDto> recupererTousLesFournisseurs() {
        return fournisseurPort.findAll().stream()
                .map(FournisseurMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FournisseurDto enregistrerNouveauFournisseur(FournisseurDto dto) {
        Fournisseur fournisseur = FournisseurMapper.toEntity(dto);
        Fournisseur saved = fournisseurPort.save(fournisseur);
        return FournisseurMapper.toDto(saved);
    }

    @Override
    public void supprimerFournisseur(Long id) {
        fournisseurPort.deleteById(id);
    }

    @Override
    public FournisseurDto rechercherFournisseurParId(Long id) {
        return FournisseurMapper.toDto(fournisseurPort.findById(id));
    }

    @Override
    public FournisseurDto rechercherFournisseurParNom(String nom) {
        return FournisseurMapper.toDto(fournisseurPort.findByNom(nom));
    }

    @Override
    public void validerContratFournisseur(Long id) {
        Fournisseur fournisseur = fournisseurPort.findById(id);
        fournisseur.setStatut(StatutFournisseur.ACTIF);
        fournisseur.setDateContrat(LocalDate.now().toString());
        fournisseurPort.save(fournisseur);
    }

    @Override
    public void resilierContratFournisseur(Long id) {
        Fournisseur fournisseur = fournisseurPort.findById(id);
        fournisseur.setStatut(StatutFournisseur.RESILIE);
        fournisseur.setDateResiliation(LocalDate.now().toString());
        fournisseurPort.save(fournisseur);
    }

    @Override
    public void suspendreFournisseur(Long id) {
        Fournisseur fournisseur = fournisseurPort.findById(id);
        fournisseur.setStatut(StatutFournisseur.INACTIF);
        fournisseurPort.save(fournisseur);
    }

    @Override
    public void reactiverFournisseur(Long id) {
        Fournisseur fournisseur = fournisseurPort.findById(id);
        fournisseur.setStatut(StatutFournisseur.ACTIF);
        fournisseurPort.save(fournisseur);
    }
}
