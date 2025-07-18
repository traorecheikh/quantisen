package com.quantisen.boisson.domaine.stockage.port;

import com.quantisen.boisson.domaine.boisson.domainModel.Boisson;
import com.quantisen.boisson.domaine.stockage.domainModel.Lot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LotPortTest {

    @Mock
    private LotPort lotPort;

    private Lot lot;
    private List<Lot> lotList;
    private Boisson boisson;

    @BeforeEach
    void setUp() {
        boisson = Boisson.builder()
                .id(1L)
                .nom("Coca Cola")
                .build();

        lot = Lot.builder()
                .id(1L)
                .numeroLot("LOT001")
                .quantiteInitiale(100)
                .quantiteActuelle(90)
                .vendable(true)
                .boisson(boisson)
                .build();

        lotList = Arrays.asList(lot,
                Lot.builder()
                        .id(2L)
                        .numeroLot("LOT002")
                        .quantiteInitiale(50)
                        .quantiteActuelle(30)
                        .vendable(true)
                        .boisson(boisson)
                        .build());
    }

    @Test
    void save_ShouldReturnSavedLot() {
        when(lotPort.save(lot)).thenReturn(lot);

        Lot result = lotPort.save(lot);

        assertNotNull(result);
        assertEquals(lot.getId(), result.getId());
        assertEquals(lot.getNumeroLot(), result.getNumeroLot());
        verify(lotPort).save(lot);
    }

    @Test
    void save_ShouldHandleNullInput() {
        when(lotPort.save(null)).thenReturn(null);

        Lot result = lotPort.save(null);

        assertNull(result);
        verify(lotPort).save(null);
    }

    @Test
    void save_ShouldHandleLotWithNullId() {
        Lot newLot = Lot.builder()
                .numeroLot("LOT003")
                .quantiteInitiale(200)
                .quantiteActuelle(200)
                .vendable(true)
                .boisson(boisson)
                .build();

        when(lotPort.save(newLot)).thenReturn(newLot);

        Lot result = lotPort.save(newLot);

        assertNotNull(result);
        assertEquals("LOT003", result.getNumeroLot());
        verify(lotPort).save(newLot);
    }

    @Test
    void saveAll_ShouldReturnSavedLotList() {
        when(lotPort.saveAll(lotList)).thenReturn(lotList);

        List<Lot> result = lotPort.saveAll(lotList);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(lotPort).saveAll(lotList);
    }

    @Test
    void saveAll_ShouldHandleEmptyList() {
        List<Lot> emptyList = List.of();
        when(lotPort.saveAll(emptyList)).thenReturn(emptyList);

        List<Lot> result = lotPort.saveAll(emptyList);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(lotPort).saveAll(emptyList);
    }

    @Test
    void saveAll_ShouldHandleNullList() {
        when(lotPort.saveAll(null)).thenReturn(null);

        List<Lot> result = lotPort.saveAll(null);

        assertNull(result);
        verify(lotPort).saveAll(null);
    }

    @Test
    void update_ShouldCallUpdateMethod() {
        doNothing().when(lotPort).update(lot);

        lotPort.update(lot);

        verify(lotPort).update(lot);
    }

    @Test
    void update_ShouldHandleNullInput() {
        doNothing().when(lotPort).update(null);

        lotPort.update(null);

        verify(lotPort).update(null);
    }

    @Test
    void findById_ShouldReturnOptionalWithLotWhenExists() {
        when(lotPort.findById(1L)).thenReturn(Optional.of(lot));

        Optional<Lot> result = lotPort.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(lot.getId(), result.get().getId());
        verify(lotPort).findById(1L);
    }

    @Test
    void findById_ShouldReturnEmptyOptionalWhenNotExists() {
        when(lotPort.findById(999L)).thenReturn(Optional.empty());

        Optional<Lot> result = lotPort.findById(999L);

        assertFalse(result.isPresent());
        verify(lotPort).findById(999L);
    }

    @Test
    void findById_ShouldHandleNullId() {
        when(lotPort.findById(null)).thenReturn(Optional.empty());

        Optional<Lot> result = lotPort.findById(null);

        assertFalse(result.isPresent());
        verify(lotPort).findById(null);
    }

    @Test
    void findValidLotsByBoissonId_ShouldReturnValidLotsForBoisson() {
        when(lotPort.findValidLotsByBoissonId(1L)).thenReturn(lotList);

        List<Lot> result = lotPort.findValidLotsByBoissonId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(lotPort).findValidLotsByBoissonId(1L);
    }

    @Test
    void findValidLotsByBoissonId_ShouldReturnEmptyListWhenNoValidLots() {
        when(lotPort.findValidLotsByBoissonId(999L)).thenReturn(List.of());

        List<Lot> result = lotPort.findValidLotsByBoissonId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(lotPort).findValidLotsByBoissonId(999L);
    }

    @Test
    void findValidLotsByBoissonId_ShouldHandleNullId() {
        when(lotPort.findValidLotsByBoissonId(null)).thenReturn(List.of());

        List<Lot> result = lotPort.findValidLotsByBoissonId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(lotPort).findValidLotsByBoissonId(null);
    }

    @Test
    void findAllValidLots_ShouldReturnAllValidLots() {
        when(lotPort.findAllValidLots()).thenReturn(lotList);

        List<Lot> result = lotPort.findAllValidLots();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(lotPort).findAllValidLots();
    }

    @Test
    void findAllValidLots_ShouldReturnEmptyListWhenNoValidLots() {
        when(lotPort.findAllValidLots()).thenReturn(List.of());

        List<Lot> result = lotPort.findAllValidLots();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(lotPort).findAllValidLots();
    }

    @Test
    void findAllLotsByBoissonId_ShouldReturnAllLotsForBoisson() {
        when(lotPort.findAllLotsByBoissonId(1L)).thenReturn(lotList);

        List<Lot> result = lotPort.findAllLotsByBoissonId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(lotPort).findAllLotsByBoissonId(1L);
    }

    @Test
    void findAllLotsByBoissonId_ShouldReturnEmptyListWhenNoLots() {
        when(lotPort.findAllLotsByBoissonId(999L)).thenReturn(List.of());

        List<Lot> result = lotPort.findAllLotsByBoissonId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(lotPort).findAllLotsByBoissonId(999L);
    }

    @Test
    void findAllLotsByBoissonId_ShouldHandleNullId() {
        when(lotPort.findAllLotsByBoissonId(null)).thenReturn(List.of());

        List<Lot> result = lotPort.findAllLotsByBoissonId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(lotPort).findAllLotsByBoissonId(null);
    }

    @Test
    void findAll_ShouldReturnAllLots() {
        when(lotPort.findAll()).thenReturn(lotList);

        List<Lot> result = lotPort.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(lotPort).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyListWhenNoLots() {
        when(lotPort.findAll()).thenReturn(List.of());

        List<Lot> result = lotPort.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(lotPort).findAll();
    }

    @Test
    void findAll_ShouldHandleNullResult() {
        when(lotPort.findAll()).thenReturn(null);

        List<Lot> result = lotPort.findAll();

        assertNull(result);
        verify(lotPort).findAll();
    }
}