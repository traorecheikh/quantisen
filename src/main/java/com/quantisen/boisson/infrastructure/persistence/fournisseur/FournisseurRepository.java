package com.quantisen.boisson.infrastructure.persistence.fournisseur;

import com.quantisen.boisson.domaine.fournisseur.domainModel.Fournisseur;
import com.quantisen.boisson.domaine.fournisseur.port.FournisseurPort;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;

import java.util.List;

@RequestScoped
@Named
public class FournisseurRepository implements FournisseurPort {


    @Inject
    private EntityManager entityManager;
    @Override
    public List<Fournisseur> findAll() {
        try {
            return entityManager.createQuery("SELECT f FROM Fournisseur f", Fournisseur.class)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all fournisseurs: " + e.getMessage(), e);
        }
    }

    @Override
    public Fournisseur save(Fournisseur fournisseur) {
        try {
            entityManager.persist(fournisseur);
            entityManager.flush();
            return fournisseur;
        } catch (Exception e) {
            throw new RuntimeException("Error saving fournisseur: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            Fournisseur fournisseur = entityManager.find(Fournisseur.class, id);
            if (fournisseur != null) {
                entityManager.remove(fournisseur);
            } else {
                throw new RuntimeException("Fournisseur not found with id: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting fournisseur: " + e.getMessage(), e);
        }
    }

    @Override
    public Fournisseur findById(Long id) {
        Fournisseur fournisseur = entityManager.find(Fournisseur.class, id);
        if (fournisseur == null) {
            throw new RuntimeException("Fournisseur not found with id: " + id);
        }
        return fournisseur;
    }

    @Override
    public List<Fournisseur> saveAll(List<Fournisseur> fournisseurs) {
        try {
            for (Fournisseur fournisseur : fournisseurs) {
                entityManager.persist(fournisseur);
            }
            entityManager.flush();
            return fournisseurs;
        } catch (Exception e) {
            throw new RuntimeException("Error saving fournisseurs: " + e.getMessage(), e);
        }
    }

    @Override
    public Fournisseur findByNom(String nom) {
        return null;
    }
}
