package com.quantisen.boisson.domaine.stockage.port;

import com.quantisen.boisson.domaine.stockage.domainModel.Mouvement;

import java.util.List;

public interface MouvementPort {
    Mouvement save(Mouvement m);

    List<Mouvement> saveAll(List<Mouvement> mouvements);

    List<Mouvement> findAll();

    List<Mouvement> findByDate(String dateDebut, String dateFin);

    List<Mouvement> findAllByLotId(Long lotId);

    List<Mouvement> findAllByBoissonId(Long boissonId);

    List<Mouvement> findAllByUtilisateurId(Long utilisateurId);
}