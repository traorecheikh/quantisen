package com.quantisen.boisson.domaine.stockage.port;

import com.quantisen.boisson.domaine.identite.domainModel.Role;
import com.quantisen.boisson.domaine.identite.domainModel.CompteUtilisateur;
import com.quantisen.boisson.domaine.stockage.domainModel.Mouvement;
import com.quantisen.boisson.domaine.stockage.enums.TypeMouvement;
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
class MouvementPortTest {

    @Mock
    private MouvementPort mouvementPort;

    private Mouvement mouvement;
    private List<Mouvement> mouvementList;
    private CompteUtilisateur utilisateur;

    @BeforeEach
    void setUp() {
        utilisateur = CompteUtilisateur.builder()
                .id(1L)
                .email("test@example.com")
                .role(Role.GERANT)
                .build();

        mouvement = Mouvement.builder()
                .id(1L)
                .type(TypeMouvement.ENTREE)
                .quantite(10)
                .utilisateur(utilisateur)
                .build();

        mouvementList = Arrays.asList(mouvement,
                Mouvement.builder()
                        .id(2L)
                        .type(TypeMouvement.SORTIE)
                        .quantite(5)
                        .utilisateur(utilisateur)
                        .build());
    }

    @Test
    void save_ShouldReturnSavedMouvement() {
        when(mouvementPort.save(mouvement)).thenReturn(mouvement);

        Mouvement result = mouvementPort.save(mouvement);

        assertNotNull(result);
        assertEquals(mouvement.getId(), result.getId());
        assertEquals(mouvement.getType(), result.getType());
        assertEquals(mouvement.getQuantite(), result.getQuantite());
        verify(mouvementPort).save(mouvement);
    }

    @Test
    void save_ShouldHandleNullInput() {
        when(mouvementPort.save(null)).thenReturn(null);

        Mouvement result = mouvementPort.save(null);

        assertNull(result);
        verify(mouvementPort).save(null);
    }

    @Test
    void save_ShouldHandleMouvementWithNullId() {
        Mouvement newMouvement = Mouvement.builder()
                .type(TypeMouvement.AJUSTEMENT)
                .quantite(15)
                .utilisateur(utilisateur)
                .build();

        when(mouvementPort.save(newMouvement)).thenReturn(newMouvement);

        Mouvement result = mouvementPort.save(newMouvement);

        assertNotNull(result);
        assertEquals(TypeMouvement.AJUSTEMENT, result.getType());
        assertEquals(15, result.getQuantite());
        verify(mouvementPort).save(newMouvement);
    }

    @Test
    void saveAll_ShouldReturnSavedMouvementList() {
        when(mouvementPort.saveAll(mouvementList)).thenReturn(mouvementList);

        List<Mouvement> result = mouvementPort.saveAll(mouvementList);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mouvementPort).saveAll(mouvementList);
    }

    @Test
    void saveAll_ShouldHandleEmptyList() {
        List<Mouvement> emptyList = List.of();
        when(mouvementPort.saveAll(emptyList)).thenReturn(emptyList);

        List<Mouvement> result = mouvementPort.saveAll(emptyList);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mouvementPort).saveAll(emptyList);
    }

    @Test
    void saveAll_ShouldHandleNullList() {
        when(mouvementPort.saveAll(null)).thenReturn(null);

        List<Mouvement> result = mouvementPort.saveAll(null);

        assertNull(result);
        verify(mouvementPort).saveAll(null);
    }

    @Test
    void findAll_ShouldReturnAllMouvements() {
        when(mouvementPort.findAll()).thenReturn(mouvementList);

        List<Mouvement> result = mouvementPort.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mouvementPort).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyListWhenNoMouvements() {
        when(mouvementPort.findAll()).thenReturn(List.of());

        List<Mouvement> result = mouvementPort.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mouvementPort).findAll();
    }

    @Test
    void findByDate_ShouldReturnMouvementsWithinDateRange() {
        String dateDebut = "2024-01-01";
        String dateFin = "2024-12-31";
        when(mouvementPort.findByDate(dateDebut, dateFin)).thenReturn(mouvementList);

        List<Mouvement> result = mouvementPort.findByDate(dateDebut, dateFin);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mouvementPort).findByDate(dateDebut, dateFin);
    }

    @Test
    void findByDate_ShouldReturnEmptyListWhenNoMouvementsInRange() {
        String dateDebut = "2023-01-01";
        String dateFin = "2023-12-31";
        when(mouvementPort.findByDate(dateDebut, dateFin)).thenReturn(List.of());

        List<Mouvement> result = mouvementPort.findByDate(dateDebut, dateFin);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mouvementPort).findByDate(dateDebut, dateFin);
    }

    @Test
    void findByDate_ShouldHandleNullDates() {
        when(mouvementPort.findByDate(null, null)).thenReturn(List.of());

        List<Mouvement> result = mouvementPort.findByDate(null, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mouvementPort).findByDate(null, null);
    }

    @Test
    void findByDate_ShouldHandleEmptyDates() {
        when(mouvementPort.findByDate("", "")).thenReturn(List.of());

        List<Mouvement> result = mouvementPort.findByDate("", "");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mouvementPort).findByDate("", "");
    }

    @Test
    void findAllByLotId_ShouldReturnMouvementsForLot() {
        when(mouvementPort.findAllByLotId(1L)).thenReturn(mouvementList);

        List<Mouvement> result = mouvementPort.findAllByLotId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mouvementPort).findAllByLotId(1L);
    }

    @Test
    void findAllByLotId_ShouldReturnEmptyListWhenNoMouvements() {
        when(mouvementPort.findAllByLotId(999L)).thenReturn(List.of());

        List<Mouvement> result = mouvementPort.findAllByLotId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mouvementPort).findAllByLotId(999L);
    }

    @Test
    void findAllByLotId_ShouldHandleNullId() {
        when(mouvementPort.findAllByLotId(null)).thenReturn(List.of());

        List<Mouvement> result = mouvementPort.findAllByLotId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mouvementPort).findAllByLotId(null);
    }

    @Test
    void findAllByBoissonId_ShouldReturnMouvementsForBoisson() {
        when(mouvementPort.findAllByBoissonId(1L)).thenReturn(mouvementList);

        List<Mouvement> result = mouvementPort.findAllByBoissonId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mouvementPort).findAllByBoissonId(1L);
    }

    @Test
    void findAllByBoissonId_ShouldReturnEmptyListWhenNoMouvements() {
        when(mouvementPort.findAllByBoissonId(999L)).thenReturn(List.of());

        List<Mouvement> result = mouvementPort.findAllByBoissonId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mouvementPort).findAllByBoissonId(999L);
    }

    @Test
    void findAllByBoissonId_ShouldHandleNullId() {
        when(mouvementPort.findAllByBoissonId(null)).thenReturn(List.of());

        List<Mouvement> result = mouvementPort.findAllByBoissonId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mouvementPort).findAllByBoissonId(null);
    }

    @Test
    void findAllByUtilisateurId_ShouldReturnMouvementsForUtilisateur() {
        when(mouvementPort.findAllByUtilisateurId(1L)).thenReturn(mouvementList);

        List<Mouvement> result = mouvementPort.findAllByUtilisateurId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mouvementPort).findAllByUtilisateurId(1L);
    }

    @Test
    void findAllByUtilisateurId_ShouldReturnEmptyListWhenNoMouvements() {
        when(mouvementPort.findAllByUtilisateurId(999L)).thenReturn(List.of());

        List<Mouvement> result = mouvementPort.findAllByUtilisateurId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mouvementPort).findAllByUtilisateurId(999L);
    }

    @Test
    void findAllByUtilisateurId_ShouldHandleNullId() {
        when(mouvementPort.findAllByUtilisateurId(null)).thenReturn(List.of());

        List<Mouvement> result = mouvementPort.findAllByUtilisateurId(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mouvementPort).findAllByUtilisateurId(null);
    }
}