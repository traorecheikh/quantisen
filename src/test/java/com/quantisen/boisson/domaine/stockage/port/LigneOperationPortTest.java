package com.quantisen.boisson.domaine.stockage.port;

import com.quantisen.boisson.domaine.stockage.domainModel.LigneOperation;
import com.quantisen.boisson.domaine.stockage.domainModel.Lot;
import com.quantisen.boisson.domaine.stockage.domainModel.Mouvement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LigneOperationPortTest {

    @Mock
    private LigneOperationPort ligneOperationPort;

    private LigneOperation ligneOperation;
    private List<LigneOperation> ligneOperationList;
    private Mouvement mouvement;
    private Lot lot;

    @BeforeEach
    void setUp() {
        mouvement = Mouvement.builder()
                .id(1L)
                .quantite(10)
                .build();

        lot = Lot.builder()
                .id(1L)
                .numeroLot("LOT001")
                .quantiteInitiale(100)
                .quantiteActuelle(90)
                .build();

        ligneOperation = LigneOperation.builder()
                .id(1L)
                .mouvement(mouvement)
                .lot(lot)
                .quantite(10)
                .build();

        ligneOperationList = Arrays.asList(ligneOperation,
                LigneOperation.builder()
                        .id(2L)
                        .mouvement(mouvement)
                        .lot(lot)
                        .quantite(5)
                        .build());
    }

    @Test
    void save_ShouldReturnSavedLigneOperation() {
        when(ligneOperationPort.save(ligneOperation)).thenReturn(ligneOperation);

        LigneOperation result = ligneOperationPort.save(ligneOperation);

        assertNotNull(result);
        assertEquals(ligneOperation.getId(), result.getId());
        assertEquals(ligneOperation.getQuantite(), result.getQuantite());
        verify(ligneOperationPort).save(ligneOperation);
    }

    @Test
    void save_ShouldHandleNullInput() {
        when(ligneOperationPort.save(null)).thenReturn(null);

        LigneOperation result = ligneOperationPort.save(null);

        assertNull(result);
        verify(ligneOperationPort).save(null);
    }

    @Test
    void save_ShouldHandleLigneOperationWithNullId() {
        LigneOperation newLigneOperation = LigneOperation.builder()
                .mouvement(mouvement)
                .lot(lot)
                .quantite(15)
                .build();

        when(ligneOperationPort.save(newLigneOperation)).thenReturn(newLigneOperation);

        LigneOperation result = ligneOperationPort.save(newLigneOperation);

        assertNotNull(result);
        assertEquals(15, result.getQuantite());
        verify(ligneOperationPort).save(newLigneOperation);
    }

    @Test
    void findByMouvementId_ShouldReturnLigneOperationsForMouvement() {
        when(ligneOperationPort.findByMouvementId(1L)).thenReturn(ligneOperationList);

        List<LigneOperation> result = ligneOperationPort.findByMouvementId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ligneOperationPort).findByMouvementId(1L);
    }

    @Test
    void findByMouvementId_ShouldReturnEmptyListWhenNoLigneOperations() {
        when(ligneOperationPort.findByMouvementId(999L)).thenReturn(List.of());

        List<LigneOperation> result = ligneOperationPort.findByMouvementId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(ligneOperationPort).findByMouvementId(999L);
    }

    @Test
    void findByMouvementId_ShouldHandleNullId() {
        when(ligneOperationPort.findByMouvementId(null)).thenReturn(List.of());

        List<LigneOperation> result = ligneOperationPort.findByMouvementId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(ligneOperationPort).findByMouvementId(null);
    }

    @Test
    void saveAll_ShouldReturnSavedLigneOperationList() {
        when(ligneOperationPort.saveAll(ligneOperationList)).thenReturn(ligneOperationList);

        List<LigneOperation> result = ligneOperationPort.saveAll(ligneOperationList);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ligneOperationPort).saveAll(ligneOperationList);
    }

    @Test
    void saveAll_ShouldHandleEmptyList() {
        List<LigneOperation> emptyList = List.of();
        when(ligneOperationPort.saveAll(emptyList)).thenReturn(emptyList);

        List<LigneOperation> result = ligneOperationPort.saveAll(emptyList);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(ligneOperationPort).saveAll(emptyList);
    }

    @Test
    void saveAll_ShouldHandleNullList() {
        when(ligneOperationPort.saveAll(null)).thenReturn(null);

        List<LigneOperation> result = ligneOperationPort.saveAll(null);

        assertNull(result);
        verify(ligneOperationPort).saveAll(null);
    }

    @Test
    void findAllByMouvementId_ShouldReturnLigneOperationsForMouvement() {
        when(ligneOperationPort.findAllByMouvementId(1L)).thenReturn(ligneOperationList);

        List<LigneOperation> result = ligneOperationPort.findAllByMouvementId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ligneOperationPort).findAllByMouvementId(1L);
    }

    @Test
    void findAllByMouvementId_ShouldReturnEmptyListWhenNoLigneOperations() {
        when(ligneOperationPort.findAllByMouvementId(999L)).thenReturn(List.of());

        List<LigneOperation> result = ligneOperationPort.findAllByMouvementId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(ligneOperationPort).findAllByMouvementId(999L);
    }

    @Test
    void findAllByMouvementId_ShouldHandleNullId() {
        when(ligneOperationPort.findAllByMouvementId(null)).thenReturn(List.of());

        List<LigneOperation> result = ligneOperationPort.findAllByMouvementId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(ligneOperationPort).findAllByMouvementId(null);
    }

    @Test
    void findAllByLotId_ShouldReturnLigneOperationsForLot() {
        when(ligneOperationPort.findAllByLotId(1L)).thenReturn(ligneOperationList);

        List<LigneOperation> result = ligneOperationPort.findAllByLotId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ligneOperationPort).findAllByLotId(1L);
    }

    @Test
    void findAllByLotId_ShouldReturnEmptyListWhenNoLigneOperations() {
        when(ligneOperationPort.findAllByLotId(999L)).thenReturn(List.of());

        List<LigneOperation> result = ligneOperationPort.findAllByLotId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(ligneOperationPort).findAllByLotId(999L);
    }

    @Test
    void findAllByLotId_ShouldHandleNullId() {
        when(ligneOperationPort.findAllByLotId(null)).thenReturn(List.of());

        List<LigneOperation> result = ligneOperationPort.findAllByLotId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(ligneOperationPort).findAllByLotId(null);
    }

    @Test
    void findAll_ShouldReturnAllLigneOperations() {
        when(ligneOperationPort.findAll()).thenReturn(ligneOperationList);

        List<LigneOperation> result = ligneOperationPort.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ligneOperationPort).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyListWhenNoLigneOperations() {
        when(ligneOperationPort.findAll()).thenReturn(List.of());

        List<LigneOperation> result = ligneOperationPort.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(ligneOperationPort).findAll();
    }

    @Test
    void findAll_ShouldHandleNullResult() {
        when(ligneOperationPort.findAll()).thenReturn(null);

        List<LigneOperation> result = ligneOperationPort.findAll();

        assertNull(result);
        verify(ligneOperationPort).findAll();
    }
}