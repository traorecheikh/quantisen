package com.quantisen.boisson.domaine.identite.port;

import com.quantisen.boisson.domaine.identite.domainModel.CompteUtilisateur;

import java.util.List;

public interface IdentitePort {
    CompteUtilisateur getByEmail(String email);

    CompteUtilisateur save(CompteUtilisateur utilisateur);

    CompteUtilisateur getById(Long id);

    List<CompteUtilisateur> saveAll(List<CompteUtilisateur> utilisateurs);

    boolean delete(Long id);

    List<CompteUtilisateur> findAll();

    CompteUtilisateur findById(Long id);
}
