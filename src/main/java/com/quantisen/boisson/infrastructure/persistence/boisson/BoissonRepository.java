package com.quantisen.boisson.infrastructure.persistence.boisson;

import com.quantisen.boisson.domaine.boisson.domainModel.Boisson;
import com.quantisen.boisson.domaine.boisson.port.BoissonPort;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.RollbackException;

import java.util.List;

@RequestScoped
@Named
public class BoissonRepository implements BoissonPort {
    @Inject
    private EntityManager em;

    @Override
    public List<Boisson> findAll() {
        try {
            return em.createQuery("select b from Boisson b", Boisson.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boisson save(Boisson boisson) {
        EntityManager emx = em;
        EntityTransaction tx = emx.getTransaction();
        try {
            if (!tx.isActive()) {
                tx.begin();
            }
            Boisson createdBoisson = emx.merge(boisson);
            emx.flush();
            tx.commit();
            return createdBoisson;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Erreur lors de la sauvegarde de la boisson: " + e.getMessage(), e);
        }
    }


    @Override
    public void deleteById(Long id) {
        try {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Boisson boisson = em.find(Boisson.class, id);
            em.remove(boisson);
            em.flush();
            tx.commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erreur lors de la suppression de la boisson: " + e.getMessage(), e);
        }
    }

    @Override
    public Boisson findById(Long id) {
        try {
            em.flush();
            return em.find(Boisson.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche de la boisson: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Boisson> saveAll(List<Boisson> boissons) {

        EntityManager emx = em;
        EntityTransaction tx = emx.getTransaction();
        for (Boisson boisson : boissons) {
            try {
                if (!tx.isActive()) {
                    tx.begin();
                }
                emx.merge(boisson);
            } catch (Exception e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw new RuntimeException("Erreur lors de la sauvegarde de la boisson: " + e.getMessage(), e);
            }
        }
        return boissons;
    }

    @Override
    public Boisson findByNom(String nom) {
        try {
            return em.createQuery("SELECT b FROM Boisson b WHERE b.nom = :nom", Boisson.class)
                    .setParameter("nom", nom)
                    .getSingleResultOrNull();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Boisson> findLowStockItems() {
        return em.createQuery(
                "SELECT b FROM Boisson b WHERE (SELECT COALESCE(SUM(l.quantiteActuelle), 0) FROM Lot l WHERE l.boisson.id = b.id AND l.vendable = true) < b.seuil",
                Boisson.class
        ).getResultList();
    }

    @Override
    public double getTotalStockValue() {
        Double total = em.createQuery(
                "SELECT SUM(l.quantiteActuelle * b.prix) " +
                        "FROM Boisson b JOIN Lot l ON b.id = l.boisson.id " +
                        "WHERE l.vendable = true",
                Double.class
        ).getSingleResult();
        return total != null ? total : 0.0;
    }

    @Override
    public List<Boisson> findExpiredProducts() {

        return em.createQuery("SELECT b FROM Boisson b JOIN Lot l ON b.id = l.boisson.id WHERE l.datePeremption < CURRENT_DATE", Boisson.class).getResultList();
    }

    @Override
    public void changeBoissonStatus(Long id) {
        EntityTransaction tx = em.getTransaction();
        try{
            if(!tx.isActive()){
                tx.begin();
            }
            Boisson boisson = findById(id);
            boisson.setActive(!boisson.isActive());
            em.merge(boisson);
            tx.commit();

        }catch (Exception e){
            if(tx.isActive()){
                tx.rollback();
            }
            throw new RollbackException("Erreur lors du changement de status: " +e.getMessage());
        }
    }
}
