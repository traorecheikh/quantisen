package com.quantisen.boisson.infrastructure.persistence.stockage;

import com.quantisen.boisson.domaine.stockage.domainModel.Lot;
import com.quantisen.boisson.domaine.stockage.port.LotPort;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

@RequestScoped
@Named
public class LotRepository implements LotPort {

    @Inject
    private EntityManager em;


    @Override
    public Lot save(Lot nouveauLot) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(nouveauLot);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
        return nouveauLot;
    }

    @Override
    public List<Lot> saveAll(List<Lot> lots) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (Lot lot : lots) {
                em.persist(lot);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
        return lots;
    }

    @Override
    public List<Lot> findValidLotsByBoissonId(Long boissonId) {
        return em.createQuery("SELECT l FROM Lot l WHERE l.boisson.id = :boissonId AND l.vendable = true", Lot.class)
                .setParameter("boissonId", boissonId)
                .getResultList();
    }

    @Override
    public List<Lot> findAllValidLots() {
        return em.createQuery("SELECT l FROM Lot l WHERE l.vendable = true", Lot.class)
                .getResultList();
    }

    @Override
    public List<Lot> findAllLotsByBoissonId(Long boissonId) {
        return em.createQuery("SELECT l FROM Lot l WHERE l.boisson.id = :boissonId", Lot.class)
                .setParameter("boissonId", boissonId)
                .getResultList();
    }

    @Override
    public List<Lot> findAll() {
        return em.createQuery("SELECT l FROM Lot l", Lot.class)
                .getResultList();
    }

    @Override
    public int countAllByBoissonId(Long id) {
        Long count = em.createQuery("SELECT COUNT(l) FROM Lot l WHERE l.boisson.id = :boissonId", Long.class)
                .setParameter("boissonId", id)
                .getSingleResult();
        return count != null ? count.intValue() : 0;
    }

    @Override
    public void update(Lot lot) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(lot);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public Optional<Lot> findById(Long id) {
        return Optional.ofNullable(em.find(Lot.class, id));
    }
}
