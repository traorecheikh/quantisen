package com.quantisen.boisson.domaine.stockage.port;

import com.quantisen.boisson.domaine.stockage.domainModel.LigneOperation;

import java.util.List;

public interface LigneOperationPort {
    LigneOperation save(LigneOperation ligne);

    List<LigneOperation> findByMouvementId(Long mouvementId);

    List<LigneOperation> saveAll(List<LigneOperation> lignes);

    List<LigneOperation> findAllByMouvementId(Long mouvementId);

    List<LigneOperation> findAllByLotId(Long mouvementId);

    List<LigneOperation> findAll();

    List<LigneOperation> findAllPaginated(int offset, int limit);

    long countAll();
}