package com.quantisen.boisson.infrastructure.persistence.stockage;

import com.quantisen.boisson.domaine.stockage.domainModel.LigneOperation;
import com.quantisen.boisson.domaine.stockage.port.LigneOperationPort;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

@RequestScoped
@Named
public class LigneOperationRepository implements LigneOperationPort {

    @Inject
    private EntityManager em;

    @Override
    public LigneOperation save(LigneOperation ligne) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(ligne);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
        return ligne;
    }

    @Override
    public List<LigneOperation> findByMouvementId(Long mouvementId) {
        return em.createQuery("SELECT lo FROM LigneOperation lo WHERE lo.mouvement.id = :mouvementId", LigneOperation.class)
                .setParameter("mouvementId", mouvementId)
                .getResultList();
    }

    @Override
    public List<LigneOperation> saveAll(List<LigneOperation> lignes) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (LigneOperation ligne : lignes) {
                em.persist(ligne);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
        return lignes;
    }

    @Override
    public List<LigneOperation> findAllByMouvementId(Long mouvementId) {
        return em.createQuery("SELECT lo FROM LigneOperation lo WHERE lo.mouvement.id = :mouvementId", LigneOperation.class)
                .setParameter("mouvementId", mouvementId)
                .getResultList();
    }

    @Override
    public List<LigneOperation> findAllByLotId(Long mouvementId) {
        return em.createQuery("SELECT lo FROM LigneOperation lo WHERE lo.lot.id = :lotId", LigneOperation.class)
                .setParameter("lotId", mouvementId)
                .getResultList();
    }

    @Override
    public List<LigneOperation> findAll() {
        return em.createQuery("SELECT lo FROM LigneOperation lo", LigneOperation.class)
                .getResultList();
    }

    @Override
    public List<LigneOperation> findAllPaginated(int offset, int limit) {
        return em.createQuery("SELECT lo FROM LigneOperation lo ORDER BY lo.id DESC", LigneOperation.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public long countAll() {
        return em.createQuery("SELECT COUNT(lo) FROM LigneOperation lo", Long.class)
                .getSingleResult();
    }
}
