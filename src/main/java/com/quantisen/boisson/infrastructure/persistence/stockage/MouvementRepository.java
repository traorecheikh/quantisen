package com.quantisen.boisson.infrastructure.persistence.stockage;

import com.quantisen.boisson.domaine.stockage.domainModel.Mouvement;
import com.quantisen.boisson.domaine.stockage.port.MouvementPort;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

@RequestScoped
@Named
public class MouvementRepository implements MouvementPort {

    @Inject
    private EntityManager em;


    @Override
    public Mouvement save(Mouvement m) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(m);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
        return m;
    }

    @Override
    public List<Mouvement> saveAll(List<Mouvement> mouvements) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (Mouvement mouvement : mouvements) {
                em.persist(mouvement);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
        return mouvements;
    }

    @Override
    public List<Mouvement> findAll() {
        return em.createQuery("SELECT m FROM Mouvement m", Mouvement.class)
                .getResultList();
    }

    @Override
    public List<Mouvement> findByDate(String dateDebut, String dateFin) {
        return em.createQuery("SELECT m FROM Mouvement m WHERE m.dateMouvement >= :dateDebut AND m.dateMouvement <= :dateFin", Mouvement.class)
                .setParameter("dateDebut", dateDebut)
                .setParameter("dateFin", dateFin)
                .getResultList();
    }

    @Override
    public List<Mouvement> findAllByLotId(Long lotId) {
        return em.createQuery("SELECT m FROM Mouvement m JOIN m.ligneOperations l WHERE l.lot.id = :lotId", Mouvement.class)
                .setParameter("lotId", lotId)
                .getResultList();
    }

    @Override
    public List<Mouvement> findAllByBoissonId(Long boissonId) {
        return em.createQuery("SELECT m FROM Mouvement m JOIN m.ligneOperations l WHERE l.lot.boisson.id = :boissonId", Mouvement.class)
                .setParameter("boissonId", boissonId)
                .getResultList();
    }

    @Override
    public List<Mouvement> findAllByUtilisateurId(Long utilisateurId) {
        return em.createQuery("SELECT m FROM Mouvement m WHERE m.utilisateur.id = :utilisateurId", Mouvement.class)
                .setParameter("utilisateurId", utilisateurId)
                .getResultList();
    }
}

