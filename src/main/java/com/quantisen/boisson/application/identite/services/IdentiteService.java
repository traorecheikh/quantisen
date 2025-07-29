package com.quantisen.boisson.application.identite.services;

import com.quantisen.boisson.application.identite.dtos.IdentiteDto;
import com.quantisen.boisson.application.identite.requests.LoginResponse;
import com.quantisen.boisson.domaine.identite.domainModel.CompteUtilisateur;

import java.util.List;

public interface IdentiteService {
    LoginResponse authentifier(String email, String motDePasse) throws Exception;

    void changerMotDePasse(Long id, String ancienMotDePasse, String nouveauMotDePasse);

    IdentiteDto register(IdentiteDto utilisateurDto);

    List<IdentiteDto> getAll();

    boolean delete(Long id);

    /**
     * Authenticates a user and returns a JWT token if successful, null otherwise.
     */
    LoginResponse authenticateAndGenerateToken(CompteUtilisateur utilisateur) throws Exception;

    IdentiteDto getUtilisateurByEmail(String mail);

    IdentiteDto getByEmail(String email);

    IdentiteDto changeStatus(Long id, boolean status);
}
