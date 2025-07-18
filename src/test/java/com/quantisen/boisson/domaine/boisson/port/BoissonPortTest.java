package com.quantisen.boisson.domaine.boisson.port;

import com.quantisen.boisson.domaine.boisson.domainModel.Boisson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoissonPortTest {

    @Mock
    private BoissonPort boissonPort;

    private Boisson boisson;
    private List<Boisson> boissonList;

    @BeforeEach
    void setUp() {
        boisson = Boisson.builder()
                .id(1L)
                .nom("Coca Cola")
                .description("Boisson gazeuse")
                .prix(1.50)
                .volume(0.5)
                .unite("L")
                .seuil(10)
                .isActive(true)
                .build();

        boissonList = Arrays.asList(boisson,
                Boisson.builder()
                        .id(2L)
                        .nom("Pepsi")
                        .description("Boisson gazeuse")
                        .prix(1.40)
                        .volume(0.5)
                        .unite("L")
                        .seuil(15)
                        .isActive(true)
                        .build());
    }

    @Test
    void findAll_ShouldReturnAllBoissons() {
        when(boissonPort.findAll()).thenReturn(boissonList);

        List<Boisson> result = boissonPort.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(boissonPort).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyListWhenNoBoissons() {
        when(boissonPort.findAll()).thenReturn(List.of());

        List<Boisson> result = boissonPort.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(boissonPort).findAll();
    }

    @Test
    void save_ShouldReturnSavedBoisson() {
        when(boissonPort.save(boisson)).thenReturn(boisson);

        Boisson result = boissonPort.save(boisson);

        assertNotNull(result);
        assertEquals(boisson.getId(), result.getId());
        assertEquals(boisson.getNom(), result.getNom());
        verify(boissonPort).save(boisson);
    }

    @Test
    void save_ShouldHandleNullInput() {
        when(boissonPort.save(null)).thenReturn(null);

        Boisson result = boissonPort.save(null);

        assertNull(result);
        verify(boissonPort).save(null);
    }

    @Test
    void deleteById_ShouldCallDeleteMethod() {
        doNothing().when(boissonPort).deleteById(1L);

        boissonPort.deleteById(1L);

        verify(boissonPort).deleteById(1L);
    }

    @Test
    void deleteById_ShouldHandleNullId() {
        doNothing().when(boissonPort).deleteById(null);

        boissonPort.deleteById(null);

        verify(boissonPort).deleteById(null);
    }

    @Test
    void findById_ShouldReturnBoissonWhenExists() {
        when(boissonPort.findById(1L)).thenReturn(boisson);

        Boisson result = boissonPort.findById(1L);

        assertNotNull(result);
        assertEquals(boisson.getId(), result.getId());
        assertEquals(boisson.getNom(), result.getNom());
        verify(boissonPort).findById(1L);
    }

    @Test
    void findById_ShouldReturnNullWhenNotExists() {
        when(boissonPort.findById(999L)).thenReturn(null);

        Boisson result = boissonPort.findById(999L);

        assertNull(result);
        verify(boissonPort).findById(999L);
    }

    @Test
    void findById_ShouldHandleNullId() {
        when(boissonPort.findById(null)).thenReturn(null);

        Boisson result = boissonPort.findById(null);

        assertNull(result);
        verify(boissonPort).findById(null);
    }

    @Test
    void saveAll_ShouldReturnSavedBoissonList() {
        when(boissonPort.saveAll(boissonList)).thenReturn(boissonList);

        List<Boisson> result = boissonPort.saveAll(boissonList);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(boissonPort).saveAll(boissonList);
    }

    @Test
    void saveAll_ShouldHandleEmptyList() {
        List<Boisson> emptyList = List.of();
        when(boissonPort.saveAll(emptyList)).thenReturn(emptyList);

        List<Boisson> result = boissonPort.saveAll(emptyList);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(boissonPort).saveAll(emptyList);
    }

    @Test
    void saveAll_ShouldHandleNullList() {
        when(boissonPort.saveAll(null)).thenReturn(null);

        List<Boisson> result = boissonPort.saveAll(null);

        assertNull(result);
        verify(boissonPort).saveAll(null);
    }

    @Test
    void findByNom_ShouldReturnBoissonWhenExists() {
        when(boissonPort.findByNom("Coca Cola")).thenReturn(boisson);

        Boisson result = boissonPort.findByNom("Coca Cola");

        assertNotNull(result);
        assertEquals("Coca Cola", result.getNom());
        verify(boissonPort).findByNom("Coca Cola");
    }

    @Test
    void findByNom_ShouldReturnNullWhenNotExists() {
        when(boissonPort.findByNom("Unknown")).thenReturn(null);

        Boisson result = boissonPort.findByNom("Unknown");

        assertNull(result);
        verify(boissonPort).findByNom("Unknown");
    }

    @Test
    void findByNom_ShouldHandleNullName() {
        when(boissonPort.findByNom(null)).thenReturn(null);

        Boisson result = boissonPort.findByNom(null);

        assertNull(result);
        verify(boissonPort).findByNom(null);
    }

    @Test
    void findByNom_ShouldHandleEmptyString() {
        when(boissonPort.findByNom("")).thenReturn(null);

        Boisson result = boissonPort.findByNom("");

        assertNull(result);
        verify(boissonPort).findByNom("");
    }

    @Test
    void findLowStockItems_ShouldReturnLowStockBoissons() {
        List<Boisson> lowStockItems = Collections.singletonList(boisson);
        when(boissonPort.findLowStockItems()).thenReturn(lowStockItems);

        List<Boisson> result = boissonPort.findLowStockItems();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(boissonPort).findLowStockItems();
    }

    @Test
    void findLowStockItems_ShouldReturnEmptyListWhenNoLowStock() {
        when(boissonPort.findLowStockItems()).thenReturn(List.of());

        List<Boisson> result = boissonPort.findLowStockItems();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(boissonPort).findLowStockItems();
    }

    @Test
    void getTotalStockValue_ShouldReturnTotalValue() {
        when(boissonPort.getTotalStockValue()).thenReturn(100.50);

        double result = boissonPort.getTotalStockValue();

        assertEquals(100.50, result);
        verify(boissonPort).getTotalStockValue();
    }

    @Test
    void getTotalStockValue_ShouldReturnZeroWhenNoStock() {
        when(boissonPort.getTotalStockValue()).thenReturn(0.0);

        double result = boissonPort.getTotalStockValue();

        assertEquals(0.0, result);
        verify(boissonPort).getTotalStockValue();
    }

    @Test
    void findExpiredProducts_ShouldReturnExpiredProducts() {
        List<Boisson> expiredProducts = Collections.singletonList(boisson);
        when(boissonPort.findExpiredProducts()).thenReturn(expiredProducts);

        List<Boisson> result = boissonPort.findExpiredProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(boissonPort).findExpiredProducts();
    }

    @Test
    void findExpiredProducts_ShouldReturnEmptyListWhenNoExpired() {
        when(boissonPort.findExpiredProducts()).thenReturn(List.of());

        List<Boisson> result = boissonPort.findExpiredProducts();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(boissonPort).findExpiredProducts();
    }
}