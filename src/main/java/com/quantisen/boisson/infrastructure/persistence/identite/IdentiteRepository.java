package com.quantisen.boisson.infrastructure.persistence.identite;

import com.quantisen.boisson.domaine.identite.domainModel.CompteUtilisateur;
import com.quantisen.boisson.domaine.identite.port.IdentitePort;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;

import java.util.List;

@RequestScoped
@Named
public class IdentiteRepository implements IdentitePort {

    @Inject
    private EntityManager entityManager;

    @Override
    public CompteUtilisateur getByEmail(String email) {
        try {
            return entityManager.createQuery("SELECT u FROM CompteUtilisateur u WHERE u.email = :email", CompteUtilisateur.class)
                    .setParameter("email", email)
                    .getSingleResultOrNull();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user by email: " + e.getMessage(), e);

        }
    }

    @Override
    public CompteUtilisateur save(CompteUtilisateur utilisateur) {

        EntityTransaction tx = entityManager.getTransaction();
        try {
            if (!tx.isActive()) {
                tx.begin();
            }
            entityManager.persist(utilisateur
            );
            entityManager.merge(utilisateur);
            entityManager.flush();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException(e);
        }
        return utilisateur;
    }

    @Override
    public CompteUtilisateur getById(Long id) {
        CompteUtilisateur utilisateur = entityManager.find(CompteUtilisateur.class, id);
        if (utilisateur == null) {
            System.err.println("User with id " + id + " not found.");
            throw new RuntimeException("User not found with id: " + id);
        } else {
            System.out.println("User found: " + utilisateur);
            return utilisateur;
        }
    }

    @Override
    public List<CompteUtilisateur> saveAll(List<CompteUtilisateur> utilisateurs) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            if (!tx.isActive()) {
                tx.begin();
            }
            for (CompteUtilisateur utilisateur : utilisateurs) {
                entityManager.merge(utilisateur);
            }
            entityManager.flush();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error saving users: " + e.getMessage(), e);
        }
        return utilisateurs;
    }

    @Override
    public boolean delete(Long id) {
        try {
            entityManager.getTransaction().begin();
            CompteUtilisateur utilisateur = entityManager.find(CompteUtilisateur.class, id);
            if (utilisateur != null) {
                entityManager.remove(utilisateur);
                entityManager.getTransaction().commit();
                return true;
            } else {
                System.err.println("User with id " + id + " not found.");
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
    }

    @Override
    public List<CompteUtilisateur> findAll() {
        return entityManager.createQuery("SELECT u FROM CompteUtilisateur u", CompteUtilisateur.class)
                .getResultList();
    }

    @Override
    public CompteUtilisateur findById(Long id) {
        CompteUtilisateur utilisateur = getById(id);
        if (utilisateur == null) {
            System.err.println("User with id " + id + " not found.");
            throw new RuntimeException("User not found with id: " + id);
        } else {
            System.out.println("User found: " + utilisateur);
            return utilisateur;
        }
    }

}


