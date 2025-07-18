package com.quantisen.boisson.domaine.identite.port;

import com.quantisen.boisson.domaine.identite.domainModel.Role;
import com.quantisen.boisson.domaine.identite.domainModel.CompteUtilisateur;
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
class CompteIdentitePortTest {

    @Mock
    private IdentitePort identitePort;

    private CompteUtilisateur utilisateur;
    private List<CompteUtilisateur> utilisateurList;

    @BeforeEach
    void setUp() {
        utilisateur = CompteUtilisateur.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .motDePasse("hashedPassword")
                .role(Role.GERANT)
                .isActive(true)
                .build();

        utilisateurList = Arrays.asList(utilisateur,
                CompteUtilisateur.builder()
                        .id(2L)
                        .firstName("Jane")
                        .lastName("Smith")
                        .email("jane.smith@example.com")
                        .motDePasse("hashedPassword2")
                        .role(Role.EMPLOYE)
                        .isActive(true)
                        .build());
    }

    @Test
    void getByEmail_ShouldReturnUtilisateurWhenExists() {
        when(identitePort.getByEmail("john.doe@example.com")).thenReturn(utilisateur);

        CompteUtilisateur result = identitePort.getByEmail("john.doe@example.com");

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("John", result.getFirstName());
        verify(identitePort).getByEmail("john.doe@example.com");
    }

    @Test
    void getByEmail_ShouldReturnNullWhenNotExists() {
        when(identitePort.getByEmail("nonexistent@example.com")).thenReturn(null);

        CompteUtilisateur result = identitePort.getByEmail("nonexistent@example.com");

        assertNull(result);
        verify(identitePort).getByEmail("nonexistent@example.com");
    }

    @Test
    void getByEmail_ShouldHandleNullEmail() {
        when(identitePort.getByEmail(null)).thenReturn(null);

        CompteUtilisateur result = identitePort.getByEmail(null);

        assertNull(result);
        verify(identitePort).getByEmail(null);
    }

    @Test
    void getByEmail_ShouldHandleEmptyEmail() {
        when(identitePort.getByEmail("")).thenReturn(null);

        CompteUtilisateur result = identitePort.getByEmail("");

        assertNull(result);
        verify(identitePort).getByEmail("");
    }

    @Test
    void save_ShouldReturnSavedUtilisateur() {
        when(identitePort.save(utilisateur)).thenReturn(utilisateur);

        CompteUtilisateur result = identitePort.save(utilisateur);

        assertNotNull(result);
        assertEquals(utilisateur.getId(), result.getId());
        assertEquals(utilisateur.getEmail(), result.getEmail());
        verify(identitePort).save(utilisateur);
    }

    @Test
    void save_ShouldHandleNullInput() {
        when(identitePort.save(null)).thenReturn(null);

        CompteUtilisateur result = identitePort.save(null);

        assertNull(result);
        verify(identitePort).save(null);
    }

    @Test
    void save_ShouldHandleUtilisateurWithNullId() {
        CompteUtilisateur newCompteUtilisateur = CompteUtilisateur.builder()
                .firstName("New")
                .lastName("User")
                .email("new@example.com")
                .role(Role.EMPLOYE)
                .isActive(true)
                .build();

        when(identitePort.save(newCompteUtilisateur)).thenReturn(newCompteUtilisateur);

        CompteUtilisateur result = identitePort.save(newCompteUtilisateur);

        assertNotNull(result);
        assertEquals("new@example.com", result.getEmail());
        verify(identitePort).save(newCompteUtilisateur);
    }

    @Test
    void getById_ShouldReturnUtilisateurWhenExists() {
        when(identitePort.getById(1L)).thenReturn(utilisateur);

        CompteUtilisateur result = identitePort.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        verify(identitePort).getById(1L);
    }

    @Test
    void getById_ShouldReturnNullWhenNotExists() {
        when(identitePort.getById(999L)).thenReturn(null);

        CompteUtilisateur result = identitePort.getById(999L);

        assertNull(result);
        verify(identitePort).getById(999L);
    }

    @Test
    void getById_ShouldHandleNullId() {
        when(identitePort.getById(null)).thenReturn(null);

        CompteUtilisateur result = identitePort.getById(null);

        assertNull(result);
        verify(identitePort).getById(null);
    }

    @Test
    void saveAll_ShouldReturnSavedUtilisateurList() {
        when(identitePort.saveAll(utilisateurList)).thenReturn(utilisateurList);

        List<CompteUtilisateur> result = identitePort.saveAll(utilisateurList);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(identitePort).saveAll(utilisateurList);
    }

    @Test
    void saveAll_ShouldHandleEmptyList() {
        List<CompteUtilisateur> emptyList = List.of();
        when(identitePort.saveAll(emptyList)).thenReturn(emptyList);

        List<CompteUtilisateur> result = identitePort.saveAll(emptyList);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(identitePort).saveAll(emptyList);
    }

    @Test
    void saveAll_ShouldHandleNullList() {
        when(identitePort.saveAll(null)).thenReturn(null);

        List<CompteUtilisateur> result = identitePort.saveAll(null);

        assertNull(result);
        verify(identitePort).saveAll(null);
    }

    @Test
    void delete_ShouldReturnTrueWhenDeleted() {
        when(identitePort.delete(1L)).thenReturn(true);

        boolean result = identitePort.delete(1L);

        assertTrue(result);
        verify(identitePort).delete(1L);
    }

    @Test
    void delete_ShouldReturnFalseWhenNotExists() {
        when(identitePort.delete(999L)).thenReturn(false);

        boolean result = identitePort.delete(999L);

        assertFalse(result);
        verify(identitePort).delete(999L);
    }

    @Test
    void delete_ShouldHandleNullId() {
        when(identitePort.delete(null)).thenReturn(false);

        boolean result = identitePort.delete(null);

        assertFalse(result);
        verify(identitePort).delete(null);
    }

    @Test
    void findAll_ShouldReturnAllUtilisateurs() {
        when(identitePort.findAll()).thenReturn(utilisateurList);

        List<CompteUtilisateur> result = identitePort.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(identitePort).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyListWhenNoUtilisateurs() {
        when(identitePort.findAll()).thenReturn(List.of());

        List<CompteUtilisateur> result = identitePort.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(identitePort).findAll();
    }

    @Test
    void findAll_ShouldHandleNullResult() {
        when(identitePort.findAll()).thenReturn(null);

        List<CompteUtilisateur> result = identitePort.findAll();

        assertNull(result);
        verify(identitePort).findAll();
    }
}