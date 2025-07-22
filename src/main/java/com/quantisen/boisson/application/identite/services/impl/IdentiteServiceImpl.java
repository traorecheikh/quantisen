package com.quantisen.boisson.application.identite.services.impl;

import com.password4j.Hash;
import com.password4j.Password;
import com.quantisen.boisson.application.identite.dtos.IdentiteDto;
import com.quantisen.boisson.application.identite.mappers.IdentiteMapper;
import com.quantisen.boisson.application.identite.requests.LoginResponse;
import com.quantisen.boisson.application.identite.services.IdentiteService;
import com.quantisen.boisson.domaine.identite.domainModel.CompteUtilisateur;
import com.quantisen.boisson.infrastructure.persistence.identite.IdentiteRepository;
import com.quantisen.boisson.infrastructure.security.JwtSession;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@ApplicationScoped
@Named
public class IdentiteServiceImpl implements IdentiteService {

    @Inject
    private IdentiteRepository repository;
    @Inject
    private JwtSession JwtSession;
    @Inject
    private IdentiteMapper mapper;

    @Override
    public LoginResponse authentifier(String email, String motDePasse) {
        try {
            CompteUtilisateur utilisateur = repository.getByEmail(email);
            System.err.println("Authenticating user: " + utilisateur.toString());
            boolean estValide = Password.check(motDePasse, utilisateur.getMotDePasse()).withArgon2();
            if (!estValide) {
                throw new RuntimeException("Email ou mot de passe incorrect");
            }
            return authenticateAndGenerateToken(utilisateur);

        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getMessage());
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean changerMotDePasse(Long id, String ancienMotDePasse, String nouveauMotDePasse) {
        CompteUtilisateur utilisateur = repository.findAll().stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
        Hash hash = Password.hash(nouveauMotDePasse).addRandomSalt().withArgon2();

        if (utilisateur != null) {
            boolean estValide = Password.check(ancienMotDePasse, utilisateur.getMotDePasse()).withArgon2();
            if (!estValide) {
                throw new RuntimeException("Email ou mot de passe incorrect");
            }
            utilisateur.setMotDePasse(hash.getResult());
            utilisateur.setActive(true);
            utilisateur.setFirstLogin(false);
            repository.save(utilisateur);
            return true;
        }
        return false;
    }

    @Override
    public IdentiteDto register(IdentiteDto utilisateurDto) {
        try {
            CompteUtilisateur utilisateurExistant = repository.getByEmail(utilisateurDto.getEmail());
            if (utilisateurExistant != null) {
                throw new RuntimeException("User with email " + utilisateurExistant + " already exists.");
            } else {
                Hash hash = Password.hash(utilisateurDto.getMotDePasse()).addRandomSalt().withArgon2();
                utilisateurDto.setMotDePasse(hash.getResult());
                CompteUtilisateur createdUser = repository.save(mapper.toEntity(utilisateurDto, true));
                System.err.println("User registered successfully: " + createdUser);
                return mapper.toDto(createdUser);
            }
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    @Override
    public List<IdentiteDto> getAll() {
        try {
            List<CompteUtilisateur> utilisateurs = repository.findAll();
            return mapper.toDtoList(utilisateurs);
        } catch (Exception e) {
            System.err.println("Error fetching users: " + e.getMessage());
            throw new RuntimeException("Error fetching users: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try {
            boolean deleted = repository.delete(id);
            if (!deleted) {
                System.err.println("User with id " + id + " not found.");
            }
            return deleted;
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }


    @Override
    public LoginResponse authenticateAndGenerateToken(CompteUtilisateur utilisateur) throws Exception {
        try {
            return new LoginResponse(utilisateur.toDto(), JwtSession.generateToken(utilisateur, 3600));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IdentiteDto getUtilisateurByEmail(String mail) {
        CompteUtilisateur utilisateur = repository.getByEmail(mail);
        if (utilisateur == null) {
            throw new RuntimeException("No user found with email: " + mail);
        }
        return mapper.toDto(utilisateur);
    }

    @Override
    public IdentiteDto getByEmail(String email) {
        CompteUtilisateur utilisateur = repository.getByEmail(email);

        return mapper.toDto(utilisateur);
    }

    @Override
    public IdentiteDto changeStatus(Long id, boolean status) {
        CompteUtilisateur utilisateur = repository.findById(id);
        if (utilisateur == null) {
            throw new RuntimeException("User with id " + id + " not found.");
        }
        utilisateur.setActive(status);
        repository.save(utilisateur);
        return mapper.toDto(utilisateur);

    }

}