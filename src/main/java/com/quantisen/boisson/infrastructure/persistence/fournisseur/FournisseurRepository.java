package com.quantisen.boisson.infrastructure.persistence.fournisseur;

import com.quantisen.boisson.domaine.fournisseur.domainModel.Fournisseur;
import com.quantisen.boisson.domaine.fournisseur.port.FournisseurPort;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

@RequestScoped @Named
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
            EntityTransaction tx = entityManager.getTransaction();
            if (!tx.isActive()) {
                tx.begin();
            }
            if( fournisseur.getId() == null) {
                entityManager.persist(fournisseur);
            } else {
                // Re-fetch the entity to ensure it's managed and up-to-date
                Fournisseur managedFournisseur = entityManager.find(Fournisseur.class, fournisseur.getId());
                if (managedFournisseur != null) {
                    // Copy updated fields from the passed-in fournisseur to the managed one
                    managedFournisseur.setStatut(fournisseur.getStatut());
                    managedFournisseur.setDateResiliation(fournisseur.getDateResiliation());
                    // ... copy other fields if they were potentially modified elsewhere
                    fournisseur = entityManager.merge(managedFournisseur);
                } else {
                    // If not found, persist as a new entity (though this case should be handled by findById in service)
                    entityManager.persist(fournisseur);
                }
            }
            entityManager.flush();
            tx.commit();
            return fournisseur;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error saving fournisseur: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            if (!tx.isActive()) {
                tx.begin();
            }
            Fournisseur fournisseur = entityManager.find(Fournisseur.class, id);
            if (fournisseur != null) {
                entityManager.remove(fournisseur);
                tx.commit();
            } else {
                tx.rollback();
                throw new RuntimeException("Fournisseur not found with id: " + id);
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
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
            EntityTransaction tx = entityManager.getTransaction();
            if (!tx.isActive()) {
                tx.begin();
            }
            for (Fournisseur fournisseur : fournisseurs) {
                entityManager.persist(fournisseur);
            }
            entityManager.flush();
            tx.commit();
            return fournisseurs;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error saving fournisseurs: " + e.getMessage(), e);
        }
    }

    @Override
    public Fournisseur findByNom(String nom) {
        return null;
    }
}