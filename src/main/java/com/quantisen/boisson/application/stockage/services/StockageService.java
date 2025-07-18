package com.quantisen.boisson.application.stockage.services;

import com.quantisen.boisson.application.identite.dtos.IdentiteDto;
import com.quantisen.boisson.application.stockage.dtos.LigneOperationDto;
import com.quantisen.boisson.application.stockage.dtos.LotDto;
import com.quantisen.boisson.application.stockage.dtos.MouvementDto;

import java.util.List;

public interface StockageService {
    LigneOperationDto entreeSimple(LotDto nouveauLot, IdentiteDto utilisateurDto);

    void sortie(Long boissonId, int quantiteDemandee, IdentiteDto user);

    void ajustement(Long lotId, int delta, String raison, IdentiteDto user);

    List<LigneOperationDto> entreeBatch(List<LotDto> lots, IdentiteDto utilisateurDto);

    List<LotDto> getAllValidLots();

    List<LotDto> getAllLotsByBoissonId(Long boissonId);

    List<LotDto> getAllLots();

    List<MouvementDto> getAllMouvementsByBoissonId(Long boissonId);

    List<MouvementDto> getAllMouvementsByLotId(Long lotId);

    List<MouvementDto> getAllMouvements();

    List<LigneOperationDto> getAllLigneOperationsByMouvementId(Long mouvementId);

    List<LigneOperationDto> getAllLigneOperationsByLotId(Long mouvementId);

    List<MouvementDto> getAllMouvementsByUtilisateur(Long utilisateurId);

    List<LigneOperationDto> getAllLigneOperations();

    List<LigneOperationDto> getLigneOperationsPaginated(int page, int size);

    long countLigneOperations();
}
