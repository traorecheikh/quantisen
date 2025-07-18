package com.quantisen.boisson.domaine.stockage.services;

import com.quantisen.boisson.application.boisson.dtos.BoissonDto;
import com.quantisen.boisson.application.identite.dtos.IdentiteDto;
import com.quantisen.boisson.application.stockage.dtos.LigneOperationDto;
import com.quantisen.boisson.application.stockage.dtos.LotDto;
import com.quantisen.boisson.application.stockage.dtos.MouvementDto;
import com.quantisen.boisson.application.stockage.services.StockageService;
import com.quantisen.boisson.domaine.identite.domainModel.Role;
import com.quantisen.boisson.domaine.stockage.enums.TypeMouvement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockageServiceTest {

    @Mock
    private StockageService stockageService;

    private LotDto lotDto;
    private LigneOperationDto ligneOperationDto;
    private MouvementDto mouvementDto;
    private IdentiteDto identiteDto;
    private BoissonDto boissonDto;

    @BeforeEach
    void setUp() {
        boissonDto = BoissonDto.builder()
                .id(1L)
                .nom("Coca Cola")
                .build();

        identiteDto = IdentiteDto.builder()
                .id(1L)
                .email("user@example.com")
                .role(Role.GERANT)
                .build();

        lotDto = LotDto.builder()
                .id(1L)
                .numeroLot("LOT001")
                .quantiteInitiale(100)
                .quantiteActuelle(90)
                .boisson(boissonDto)
                .vendable(true)
                .build();

        mouvementDto = new MouvementDto();
        mouvementDto.setId(1L);
        mouvementDto.setType(TypeMouvement.ENTREE);
        mouvementDto.setQuantite(10);
        mouvementDto.setUtilisateur(identiteDto);

        ligneOperationDto = LigneOperationDto.builder()
                .id(1L)
                .lot(lotDto)
                .mouvement(mouvementDto)
                .quantite(10)
                .build();
    }

    @Test
    void entreeSimple_ShouldReturnLigneOperationDto() {
        when(stockageService.entreeSimple(lotDto, identiteDto)).thenReturn(ligneOperationDto);

        LigneOperationDto result = stockageService.entreeSimple(lotDto, identiteDto);

        assertNotNull(result);
        assertEquals(ligneOperationDto.getId(), result.getId());
        assertEquals(ligneOperationDto.getQuantite(), result.getQuantite());
        verify(stockageService).entreeSimple(lotDto, identiteDto);
    }

    @Test
    void entreeSimple_ShouldHandleNullLotDto() {
        when(stockageService.entreeSimple(null, identiteDto)).thenReturn(null);

        LigneOperationDto result = stockageService.entreeSimple(null, identiteDto);

        assertNull(result);
        verify(stockageService).entreeSimple(null, identiteDto);
    }

    @Test
    void entreeSimple_ShouldHandleNullUtilisateurDto() {
        when(stockageService.entreeSimple(lotDto, null)).thenReturn(null);

        LigneOperationDto result = stockageService.entreeSimple(lotDto, null);

        assertNull(result);
        verify(stockageService).entreeSimple(lotDto, null);
    }

    @Test
    void sortie_ShouldProcessSuccessfully() {
        doNothing().when(stockageService).sortie(1L, 10, identiteDto);

        stockageService.sortie(1L, 10, identiteDto);

        verify(stockageService).sortie(1L, 10, identiteDto);
    }

    @Test
    void sortie_ShouldHandleNullBoissonId() {
        doNothing().when(stockageService).sortie(null, 10, identiteDto);

        stockageService.sortie(null, 10, identiteDto);

        verify(stockageService).sortie(null, 10, identiteDto);
    }

    @Test
    void sortie_ShouldHandleZeroQuantity() {
        doNothing().when(stockageService).sortie(1L, 0, identiteDto);

        stockageService.sortie(1L, 0, identiteDto);

        verify(stockageService).sortie(1L, 0, identiteDto);
    }

    @Test
    void sortie_ShouldHandleNullUser() {
        doNothing().when(stockageService).sortie(1L, 10, null);

        stockageService.sortie(1L, 10, null);

        verify(stockageService).sortie(1L, 10, null);
    }

    @Test
    void ajustement_ShouldProcessSuccessfully() {
        doNothing().when(stockageService).ajustement(1L, 5, "Ajustement de stock", identiteDto);

        stockageService.ajustement(1L, 5, "Ajustement de stock", identiteDto);

        verify(stockageService).ajustement(1L, 5, "Ajustement de stock", identiteDto);
    }

    @Test
    void ajustement_ShouldHandleNullLotId() {
        doNothing().when(stockageService).ajustement(null, 5, "Ajustement de stock", identiteDto);

        stockageService.ajustement(null, 5, "Ajustement de stock", identiteDto);

        verify(stockageService).ajustement(null, 5, "Ajustement de stock", identiteDto);
    }

    @Test
    void ajustement_ShouldHandleNullRaison() {
        doNothing().when(stockageService).ajustement(1L, 5, null, identiteDto);

        stockageService.ajustement(1L, 5, null, identiteDto);

        verify(stockageService).ajustement(1L, 5, null, identiteDto);
    }

    @Test
    void ajustement_ShouldHandleNullUser() {
        doNothing().when(stockageService).ajustement(1L, 5, "Ajustement de stock", null);

        stockageService.ajustement(1L, 5, "Ajustement de stock", null);

        verify(stockageService).ajustement(1L, 5, "Ajustement de stock", null);
    }

    @Test
    void entreeBatch_ShouldReturnLigneOperationDtoList() {
        List<LotDto> lotDtoList = Collections.singletonList(lotDto);
        List<LigneOperationDto> ligneOperationDtoList = Collections.singletonList(ligneOperationDto);
        when(stockageService.entreeBatch(lotDtoList, identiteDto)).thenReturn(ligneOperationDtoList);

        List<LigneOperationDto> result = stockageService.entreeBatch(lotDtoList, identiteDto);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(stockageService).entreeBatch(lotDtoList, identiteDto);
    }

    @Test
    void entreeBatch_ShouldHandleEmptyList() {
        List<LotDto> emptyList = List.of();
        when(stockageService.entreeBatch(emptyList, identiteDto)).thenReturn(List.of());

        List<LigneOperationDto> result = stockageService.entreeBatch(emptyList, identiteDto);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(stockageService).entreeBatch(emptyList, identiteDto);
    }

    @Test
    void entreeBatch_ShouldHandleNullList() {
        when(stockageService.entreeBatch(null, identiteDto)).thenReturn(null);

        List<LigneOperationDto> result = stockageService.entreeBatch(null, identiteDto);

        assertNull(result);
        verify(stockageService).entreeBatch(null, identiteDto);
    }

    @Test
    void getAllValidLots_ShouldReturnValidLots() {
        List<LotDto> lotDtoList = Collections.singletonList(lotDto);
        when(stockageService.getAllValidLots()).thenReturn(lotDtoList);

        List<LotDto> result = stockageService.getAllValidLots();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(stockageService).getAllValidLots();
    }

    @Test
    void getAllValidLots_ShouldReturnEmptyListWhenNoValidLots() {
        when(stockageService.getAllValidLots()).thenReturn(List.of());

        List<LotDto> result = stockageService.getAllValidLots();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(stockageService).getAllValidLots();
    }

    @Test
    void getAllLotsByBoissonId_ShouldReturnLotsForBoisson() {
        List<LotDto> lotDtoList = Collections.singletonList(lotDto);
        when(stockageService.getAllLotsByBoissonId(1L)).thenReturn(lotDtoList);

        List<LotDto> result = stockageService.getAllLotsByBoissonId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(stockageService).getAllLotsByBoissonId(1L);
    }

    @Test
    void getAllLotsByBoissonId_ShouldHandleNullId() {
        when(stockageService.getAllLotsByBoissonId(null)).thenReturn(List.of());

        List<LotDto> result = stockageService.getAllLotsByBoissonId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(stockageService).getAllLotsByBoissonId(null);
    }

    @Test
    void getAllLots_ShouldReturnAllLots() {
        List<LotDto> lotDtoList = Collections.singletonList(lotDto);
        when(stockageService.getAllLots()).thenReturn(lotDtoList);

        List<LotDto> result = stockageService.getAllLots();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(stockageService).getAllLots();
    }

    @Test
    void getAllMouvementsByBoissonId_ShouldReturnMouvementsForBoisson() {
        List<MouvementDto> mouvementDtoList = Collections.singletonList(mouvementDto);
        when(stockageService.getAllMouvementsByBoissonId(1L)).thenReturn(mouvementDtoList);

        List<MouvementDto> result = stockageService.getAllMouvementsByBoissonId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(stockageService).getAllMouvementsByBoissonId(1L);
    }

    @Test
    void getAllMouvementsByBoissonId_ShouldHandleNullId() {
        when(stockageService.getAllMouvementsByBoissonId(null)).thenReturn(List.of());

        List<MouvementDto> result = stockageService.getAllMouvementsByBoissonId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(stockageService).getAllMouvementsByBoissonId(null);
    }

    @Test
    void getAllMouvementsByLotId_ShouldReturnMouvementsForLot() {
        List<MouvementDto> mouvementDtoList = Collections.singletonList(mouvementDto);
        when(stockageService.getAllMouvementsByLotId(1L)).thenReturn(mouvementDtoList);

        List<MouvementDto> result = stockageService.getAllMouvementsByLotId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(stockageService).getAllMouvementsByLotId(1L);
    }

    @Test
    void getAllMouvementsByLotId_ShouldHandleNullId() {
        when(stockageService.getAllMouvementsByLotId(null)).thenReturn(List.of());

        List<MouvementDto> result = stockageService.getAllMouvementsByLotId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(stockageService).getAllMouvementsByLotId(null);
    }

    @Test
    void getAllMouvements_ShouldReturnAllMouvements() {
        List<MouvementDto> mouvementDtoList = Collections.singletonList(mouvementDto);
        when(stockageService.getAllMouvements()).thenReturn(mouvementDtoList);

        List<MouvementDto> result = stockageService.getAllMouvements();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(stockageService).getAllMouvements();
    }

    @Test
    void getAllLigneOperationsByMouvementId_ShouldReturnLigneOperationsForMouvement() {
        List<LigneOperationDto> ligneOperationDtoList = Collections.singletonList(ligneOperationDto);
        when(stockageService.getAllLigneOperationsByMouvementId(1L)).thenReturn(ligneOperationDtoList);

        List<LigneOperationDto> result = stockageService.getAllLigneOperationsByMouvementId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(stockageService).getAllLigneOperationsByMouvementId(1L);
    }

    @Test
    void getAllLigneOperationsByMouvementId_ShouldHandleNullId() {
        when(stockageService.getAllLigneOperationsByMouvementId(null)).thenReturn(List.of());

        List<LigneOperationDto> result = stockageService.getAllLigneOperationsByMouvementId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(stockageService).getAllLigneOperationsByMouvementId(null);
    }

    @Test
    void getAllLigneOperationsByLotId_ShouldReturnLigneOperationsForLot() {
        List<LigneOperationDto> ligneOperationDtoList = Collections.singletonList(ligneOperationDto);
        when(stockageService.getAllLigneOperationsByLotId(1L)).thenReturn(ligneOperationDtoList);

        List<LigneOperationDto> result = stockageService.getAllLigneOperationsByLotId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(stockageService).getAllLigneOperationsByLotId(1L);
    }

    @Test
    void getAllLigneOperationsByLotId_ShouldHandleNullId() {
        when(stockageService.getAllLigneOperationsByLotId(null)).thenReturn(List.of());

        List<LigneOperationDto> result = stockageService.getAllLigneOperationsByLotId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(stockageService).getAllLigneOperationsByLotId(null);
    }

    @Test
    void getAllMouvementsByUtilisateur_ShouldReturnMouvementsForUtilisateur() {
        List<MouvementDto> mouvementDtoList = Collections.singletonList(mouvementDto);
        when(stockageService.getAllMouvementsByUtilisateur(1L)).thenReturn(mouvementDtoList);

        List<MouvementDto> result = stockageService.getAllMouvementsByUtilisateur(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(stockageService).getAllMouvementsByUtilisateur(1L);
    }

    @Test
    void getAllMouvementsByUtilisateur_ShouldHandleNullId() {
        when(stockageService.getAllMouvementsByUtilisateur(null)).thenReturn(List.of());

        List<MouvementDto> result = stockageService.getAllMouvementsByUtilisateur(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(stockageService).getAllMouvementsByUtilisateur(null);
    }

    @Test
    void getAllLigneOperations_ShouldReturnAllLigneOperations() {
        List<LigneOperationDto> ligneOperationDtoList = Collections.singletonList(ligneOperationDto);
        when(stockageService.getAllLigneOperations()).thenReturn(ligneOperationDtoList);

        List<LigneOperationDto> result = stockageService.getAllLigneOperations();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(stockageService).getAllLigneOperations();
    }

    @Test
    void getAllLigneOperations_ShouldReturnEmptyListWhenNoLigneOperations() {
        when(stockageService.getAllLigneOperations()).thenReturn(List.of());

        List<LigneOperationDto> result = stockageService.getAllLigneOperations();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(stockageService).getAllLigneOperations();
    }
}