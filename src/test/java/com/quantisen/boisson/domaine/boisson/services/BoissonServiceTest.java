package com.quantisen.boisson.domaine.boisson.services;

import com.quantisen.boisson.application.boisson.dtos.BoissonDto;
import com.quantisen.boisson.application.boisson.services.BoissonService;
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
class BoissonServiceTest {

    @Mock
    private BoissonService boissonService;

    private BoissonDto boissonDto;
    private List<BoissonDto> boissonDtoList;

    @BeforeEach
    void setUp() {
        boissonDto = BoissonDto.builder()
                .id(1L)
                .nom("Coca Cola")
                .description("Boisson gazeuse")
                .prix(1.50)
                .volume(0.5)
                .unite("L")
                .seuil(10)
                .isActive(true)
                .build();

        boissonDtoList = Arrays.asList(boissonDto,
                BoissonDto.builder()
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
    void ajouterBoisson_ShouldReturnSavedBoissonDto() {
        when(boissonService.ajouterBoisson(boissonDto)).thenReturn(boissonDto);

        BoissonDto result = boissonService.ajouterBoisson(boissonDto);

        assertNotNull(result);
        assertEquals(boissonDto.getId(), result.getId());
        assertEquals(boissonDto.getNom(), result.getNom());
        verify(boissonService).ajouterBoisson(boissonDto);
    }

    @Test
    void ajouterBoisson_ShouldHandleNullInput() {
        when(boissonService.ajouterBoisson(null)).thenReturn(null);

        BoissonDto result = boissonService.ajouterBoisson(null);

        assertNull(result);
        verify(boissonService).ajouterBoisson(null);
    }

    @Test
    void ajouterBoisson_ShouldHandleBoissonWithNullId() {
        BoissonDto newBoisson = BoissonDto.builder()
                .nom("Sprite")
                .description("Boisson gazeuse")
                .prix(1.30)
                .volume(0.5)
                .unite("L")
                .seuil(20)
                .isActive(true)
                .build();

        when(boissonService.ajouterBoisson(newBoisson)).thenReturn(newBoisson);

        BoissonDto result = boissonService.ajouterBoisson(newBoisson);

        assertNotNull(result);
        assertEquals("Sprite", result.getNom());
        verify(boissonService).ajouterBoisson(newBoisson);
    }

    @Test
    void supprimerBoisson_ShouldCallDeleteMethod() {
        doNothing().when(boissonService).supprimerBoisson(1L);

        boissonService.supprimerBoisson(1L);

        verify(boissonService).supprimerBoisson(1L);
    }

    @Test
    void supprimerBoisson_ShouldHandleNullId() {
        doNothing().when(boissonService).supprimerBoisson(null);

        boissonService.supprimerBoisson(null);

        verify(boissonService).supprimerBoisson(null);
    }

    @Test
    void supprimerBoisson_ShouldHandleNonExistentId() {
        doNothing().when(boissonService).supprimerBoisson(999L);

        boissonService.supprimerBoisson(999L);

        verify(boissonService).supprimerBoisson(999L);
    }

    @Test
    void modifierBoisson_ShouldReturnModifiedBoissonDto() {
        BoissonDto modifiedBoisson = BoissonDto.builder()
                .id(1L)
                .nom("Coca Cola Updated")
                .description("Boisson gazeuse updated")
                .prix(1.60)
                .volume(0.5)
                .unite("L")
                .seuil(10)
                .isActive(true)
                .build();

        when(boissonService.modifierBoisson(modifiedBoisson)).thenReturn(modifiedBoisson);

        BoissonDto result = boissonService.modifierBoisson(modifiedBoisson);

        assertNotNull(result);
        assertEquals("Coca Cola Updated", result.getNom());
        assertEquals(1.60, result.getPrix());
        verify(boissonService).modifierBoisson(modifiedBoisson);
    }

    @Test
    void modifierBoisson_ShouldHandleNullInput() {
        when(boissonService.modifierBoisson(null)).thenReturn(null);

        BoissonDto result = boissonService.modifierBoisson(null);

        assertNull(result);
        verify(boissonService).modifierBoisson(null);
    }

    @Test
    void getBoissonById_ShouldReturnBoissonDtoWhenExists() {
        when(boissonService.getBoissonById(1L)).thenReturn(boissonDto);

        BoissonDto result = boissonService.getBoissonById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Coca Cola", result.getNom());
        verify(boissonService).getBoissonById(1L);
    }

    @Test
    void getBoissonById_ShouldReturnNullWhenNotExists() {
        when(boissonService.getBoissonById(999L)).thenReturn(null);

        BoissonDto result = boissonService.getBoissonById(999L);

        assertNull(result);
        verify(boissonService).getBoissonById(999L);
    }

    @Test
    void getBoissonById_ShouldHandleNullId() {
        when(boissonService.getBoissonById(null)).thenReturn(null);

        BoissonDto result = boissonService.getBoissonById(null);

        assertNull(result);
        verify(boissonService).getBoissonById(null);
    }

    @Test
    void getBoissonByNom_ShouldReturnBoissonDtoWhenExists() {
        when(boissonService.getBoissonByNom("Coca Cola")).thenReturn(boissonDto);

        BoissonDto result = boissonService.getBoissonByNom("Coca Cola");

        assertNotNull(result);
        assertEquals("Coca Cola", result.getNom());
        verify(boissonService).getBoissonByNom("Coca Cola");
    }

    @Test
    void getBoissonByNom_ShouldReturnNullWhenNotExists() {
        when(boissonService.getBoissonByNom("Unknown")).thenReturn(null);

        BoissonDto result = boissonService.getBoissonByNom("Unknown");

        assertNull(result);
        verify(boissonService).getBoissonByNom("Unknown");
    }

    @Test
    void getBoissonByNom_ShouldHandleNullName() {
        when(boissonService.getBoissonByNom(null)).thenReturn(null);

        BoissonDto result = boissonService.getBoissonByNom(null);

        assertNull(result);
        verify(boissonService).getBoissonByNom(null);
    }

    @Test
    void getBoissonByNom_ShouldHandleEmptyName() {
        when(boissonService.getBoissonByNom("")).thenReturn(null);

        BoissonDto result = boissonService.getBoissonByNom("");

        assertNull(result);
        verify(boissonService).getBoissonByNom("");
    }

    @Test
    void getAllBoisson_ShouldReturnAllBoissons() {
        when(boissonService.getAllBoisson()).thenReturn(boissonDtoList);

        List<BoissonDto> result = boissonService.getAllBoisson();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(boissonService).getAllBoisson();
    }

    @Test
    void getAllBoisson_ShouldReturnEmptyListWhenNoBoissons() {
        when(boissonService.getAllBoisson()).thenReturn(List.of());

        List<BoissonDto> result = boissonService.getAllBoisson();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(boissonService).getAllBoisson();
    }

    @Test
    void getAllBoissonActive_ShouldReturnActiveBoissons() {
        List<BoissonDto> activeBoissons = Collections.singletonList(boissonDto);
        when(boissonService.getAllBoissonActive()).thenReturn(activeBoissons);

        List<BoissonDto> result = boissonService.getAllBoissonActive();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isActive());
        verify(boissonService).getAllBoissonActive();
    }

    @Test
    void getAllBoissonActive_ShouldReturnEmptyListWhenNoActiveBoissons() {
        when(boissonService.getAllBoissonActive()).thenReturn(List.of());

        List<BoissonDto> result = boissonService.getAllBoissonActive();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(boissonService).getAllBoissonActive();
    }

    @Test
    void getAllBoissonInactive_ShouldReturnInactiveBoissons() {
        BoissonDto inactiveBoisson = BoissonDto.builder()
                .id(3L)
                .nom("Inactive Drink")
                .isActive(false)
                .build();
        List<BoissonDto> inactiveBoissons = Collections.singletonList(inactiveBoisson);
        when(boissonService.getAllBoissonInactive()).thenReturn(inactiveBoissons);

        List<BoissonDto> result = boissonService.getAllBoissonInactive();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.get(0).isActive());
        verify(boissonService).getAllBoissonInactive();
    }

    @Test
    void getAllBoissonInactive_ShouldReturnEmptyListWhenNoInactiveBoissons() {
        when(boissonService.getAllBoissonInactive()).thenReturn(List.of());

        List<BoissonDto> result = boissonService.getAllBoissonInactive();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(boissonService).getAllBoissonInactive();
    }
}