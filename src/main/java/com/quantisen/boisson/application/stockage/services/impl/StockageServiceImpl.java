package com.quantisen.boisson.application.stockage.services.impl;

import com.quantisen.boisson.application.identite.dtos.IdentiteDto;
import com.quantisen.boisson.application.stockage.dtos.LigneOperationDto;
import com.quantisen.boisson.application.stockage.dtos.LotDto;
import com.quantisen.boisson.application.stockage.dtos.MouvementDto;
import com.quantisen.boisson.application.stockage.exceptions.*;
import com.quantisen.boisson.application.stockage.services.StockageService;
import com.quantisen.boisson.domaine.stockage.domainModel.LigneOperation;
import com.quantisen.boisson.domaine.stockage.domainModel.Lot;
import com.quantisen.boisson.domaine.stockage.domainModel.Mouvement;
import com.quantisen.boisson.domaine.stockage.enums.TypeAjustement;
import com.quantisen.boisson.domaine.stockage.enums.TypeMouvement;
import com.quantisen.boisson.domaine.stockage.port.LigneOperationPort;
import com.quantisen.boisson.domaine.stockage.port.LotPort;
import com.quantisen.boisson.domaine.stockage.port.MouvementPort;
import com.quantisen.boisson.infrastructure.persistence.boisson.BoissonRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Comparator;
import java.util.List;

@ApplicationScoped
@Named
public class StockageServiceImpl implements StockageService {
    @Inject
    private LigneOperationPort ligneOperationRepository;
    @Inject
    private LotPort lotRepository;
    @Inject
    private MouvementPort mouvementRepository;
    @Inject
    BoissonRepository boissonRepository;

    /**
     * Entrée simple d’un lot
     */
    public LigneOperationDto entreeSimple(LotDto nouveauLot, IdentiteDto utilisateur) {
        Mouvement m = Mouvement.builder()
                .type(TypeMouvement.ENTREE)
                .quantite(nouveauLot.getQuantiteInitiale())
                .utilisateur(utilisateur.toEntity(false))
                .build();
        m = mouvementRepository.save(m);

        Lot lot = nouveauLot.toEntity();
        lot.setMouvementEntree(m);
        LigneOperation op = LigneOperation.builder()
                .mouvement(m)
                .lot(lot)
                .quantite(nouveauLot.getQuantiteInitiale())
                .build();
        lotRepository.save(lot);

        return ligneOperationRepository.save(op).toDto(true);
    }

    /**
     * Entrée batch de plusieurs
     * public List<Lot> entreeBatch(List<Lot> lots) {
     * List<Lot> saved = new ArrayList<>();
     * for (Lot l : lots) {
     * saved.add(entreeSimple(l));
     * }
     * return saved;
     * }
     * <p>
     * /** Sortie de N unités d’une boisson (FEFO + FIFO)
     */
    public void sortie(Long boissonId, int quantiteDemandee, IdentiteDto user) {
        if (quantiteDemandee <= 0) {
            throw new QuantiteDemandeeInvalideException();
        }
        if (boissonId == null) {
            throw new BoissonIdInvalideException();
        }
        if (user == null || user.getId() == null) {
            throw new UtilisateurNonAuthentifieException();
        }
        int quantiteBoisson = boissonRepository.getTotalStockBoissonById(boissonId);
        if(quantiteBoisson < quantiteDemandee) {
            throw new StockInsuffisantException();
        }
        Mouvement sortie = Mouvement.builder()
                .type(TypeMouvement.SORTIE)
                .quantite(quantiteDemandee)
                .utilisateur(user.toEntity(false))
                .build();
        sortie = mouvementRepository.save(sortie);

        List<Lot> lots = lotRepository.findValidLotsByBoissonId(boissonId);
        lots.sort(Comparator
                .comparing(Lot::getDatePeremption)
                .thenComparing(Lot::getDateEntree));

        int restant = quantiteDemandee; // 250 oeufs
        // 250
        for (Lot lot : lots) {
            //1
            if (restant == 0) break;
            //30 // 30 // 30 // 30
            //250 // 220 // 190 // 15
            int prelevable = Math.min(lot.getQuantiteActuelle(), restant);
            //30
            //30
            //30
            //15

            LigneOperation op = LigneOperation.builder()
                    .mouvement(sortie)
                    .lot(lot)
                    .quantite(prelevable)
                    .build();
            ligneOperationRepository.save(op);
            lot.setQuantiteActuelle(lot.getQuantiteActuelle() - prelevable);
            if (lot.getQuantiteActuelle() == prelevable) {
                lot.setVendable(false);
            }
            lotRepository.update(lot);
            restant -= prelevable;
        }
        if (restant > 0) {
            throw new IllegalStateException("Stock insuffisant pour la sortie demandée");
        }
    }

    /**
     * Ajustement de stock (positif ou négatif)
     */
    public void ajustement(Long lotId, int delta, String raison, IdentiteDto user) {
        Mouvement m = Mouvement.builder()
                .type(TypeMouvement.AJUSTEMENT)
                .quantite(Math.abs(delta))
                .typeAjustement(delta > 0 ? TypeAjustement.POSITIF : TypeAjustement.NEGATIF)
                .raison(raison)
                .utilisateur(user.toEntity(false))
                .build();

        Lot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> new IllegalArgumentException("Lot introuvable"));
        m = mouvementRepository.save(m);

        int nouvelleQte = lot.getQuantiteActuelle() + delta;
        if (nouvelleQte < 0 || nouvelleQte > lot.getQuantiteInitiale()) {
            throw new IllegalStateException("Ajustement hors bornes pour le lot");
        }

        lot.setQuantiteActuelle(nouvelleQte);
        lotRepository.update(lot);

        LigneOperation op = LigneOperation.builder()
                .mouvement(m)
                .lot(lot)
                .quantite(Math.abs(delta))
                .build();
        ligneOperationRepository.save(op);
    }

    @Override
    public List<LigneOperationDto> entreeBatch(List<LotDto> lotDtos, IdentiteDto utilisateur) {
        return lotDtos.stream()
                .map(lotDto -> {
                    Lot lot = lotDto.toEntity();
                    Mouvement m = Mouvement.builder()
                            .type(TypeMouvement.ENTREE)
                            .quantite(lot.getQuantiteInitiale())
                            .utilisateur(utilisateur.toEntity(false))
                            .build();
                    m = mouvementRepository.save(m);
                    lot.setMouvementEntree(m);
                    lot = lotRepository.save(lot);
                    LigneOperation op = LigneOperation.builder()
                            .mouvement(m)
                            .lot(lot)
                            .quantite(lot.getQuantiteInitiale())
                            .build();
                    return ligneOperationRepository.save(op).toDto(true);
                })
                .toList();
    }

    @Override
    public List<LotDto> getAllValidLots() {
        return lotRepository.findAllValidLots().stream()
                .map(Lot::toDto)
                .toList();
    }

    @Override
    public List<LotDto> getAllLotsByBoissonId(Long boissonId) {
        return lotRepository.findAllLotsByBoissonId(boissonId).stream()
                .map(Lot::toDto)
                .toList();
    }

    @Override
    public List<LotDto> getAllLots() {
        return lotRepository.findAll().stream()
                .map(Lot::toDto)
                .toList();
    }

    @Override
    public List<MouvementDto> getAllMouvementsByBoissonId(Long boissonId) {
        return mouvementRepository.findAllByBoissonId(boissonId).stream()
                .map(Mouvement::toDto)
                .toList();
    }

    @Override
    public List<MouvementDto> getAllMouvementsByLotId(Long lotId) {
        return mouvementRepository.findAllByLotId(lotId).stream()
                .map(Mouvement::toDto)
                .toList();
    }

    @Override
    public List<MouvementDto> getAllMouvementsByUtilisateur(Long utilisateurId) {
        return mouvementRepository.findAllByUtilisateurId(utilisateurId).stream()
                .map(Mouvement::toDto)
                .toList();
    }

    @Override
    public List<LigneOperationDto> getAllLigneOperations() {
        return ligneOperationRepository.findAll().stream()
                .map(L -> L.toDto(true))
                .toList();
    }

    @Override
    public List<MouvementDto> getAllMouvements() {
        return mouvementRepository.findAll().stream()
                .map(Mouvement::toDto)
                .toList();
    }

    @Override
    public List<LigneOperationDto> getAllLigneOperationsByMouvementId(Long mouvementId) {
        return ligneOperationRepository.findAllByMouvementId(mouvementId).stream()
                .map(L -> L.toDto(false))
                .toList();
    }

    @Override
    public List<LigneOperationDto> getAllLigneOperationsByLotId(Long mouvementId) {
        return ligneOperationRepository.findAllByLotId(mouvementId).stream()
                .map(L -> L.toDto(false))
                .toList();
    }

    @Override
    public List<LigneOperationDto> getLigneOperationsPaginated(int page, int size) {
        int offset = (page - 1) * size;
        return ligneOperationRepository.findAllPaginated(offset, size)
                .stream()
                .map(l -> l.toDto(true))
                .toList();
    }

    @Override
    public long countLigneOperations() {
        return ligneOperationRepository.countAll();
    }
}
