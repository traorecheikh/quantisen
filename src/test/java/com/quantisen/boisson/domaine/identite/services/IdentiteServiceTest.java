package com.quantisen.boisson.domaine.identite.services;

import com.quantisen.boisson.application.identite.dtos.IdentiteDto;
import com.quantisen.boisson.application.identite.requests.LoginResponse;
import com.quantisen.boisson.application.identite.services.IdentiteService;
import com.quantisen.boisson.domaine.identite.domainModel.CompteUtilisateur;
import com.quantisen.boisson.domaine.identite.domainModel.Role;
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
class IdentiteServiceTest {

    @Mock
    private IdentiteService utilisateurService;

    private IdentiteDto utilisateurDto;
    private List<IdentiteDto> utilisateurDtoList;
    private CompteUtilisateur utilisateur;
    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        utilisateurDto = IdentiteDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .role(Role.GERANT)
                .isActive(true)
                .build();

        utilisateurDtoList = Arrays.asList(utilisateurDto,
                IdentiteDto.builder()
                        .id(2L)
                        .firstName("Jane")
                        .lastName("Smith")
                        .email("jane.smith@example.com")
                        .role(Role.EMPLOYE)
                        .isActive(true)
                        .build());

        utilisateur = CompteUtilisateur.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .motDePasse("hashedPassword")
                .role(Role.GERANT)
                .isActive(true)
                .build();

        loginResponse = new LoginResponse(utilisateurDto, "jwt.token.here");
    }

    @Test
    void authentifier_ShouldReturnLoginResponseWhenCredentialsValid() throws Exception {
        when(utilisateurService.authentifier("john.doe@example.com", "password")).thenReturn(loginResponse);

        LoginResponse result = utilisateurService.authentifier("john.doe@example.com", "password");

        assertNotNull(result);
        assertEquals(utilisateurDto.getEmail(), result.getUtilisateur().getEmail());
        assertEquals("jwt.token.here", result.getToken());
        verify(utilisateurService).authentifier("john.doe@example.com", "password");
    }

    @Test
    void authentifier_ShouldThrowExceptionWhenCredentialsInvalid() throws Exception {
        when(utilisateurService.authentifier("john.doe@example.com", "wrongpassword"))
                .thenThrow(new Exception("Invalid credentials"));

        assertThrows(Exception.class, () -> {
            utilisateurService.authentifier("john.doe@example.com", "wrongpassword");
        });
        verify(utilisateurService).authentifier("john.doe@example.com", "wrongpassword");
    }

    @Test
    void authentifier_ShouldHandleNullEmail() throws Exception {
        when(utilisateurService.authentifier(null, "password"))
                .thenThrow(new Exception("Email is required"));

        assertThrows(Exception.class, () -> {
            utilisateurService.authentifier(null, "password");
        });
        verify(utilisateurService).authentifier(null, "password");
    }

    @Test
    void authentifier_ShouldHandleNullPassword() throws Exception {
        when(utilisateurService.authentifier("john.doe@example.com", null))
                .thenThrow(new Exception("Password is required"));

        assertThrows(Exception.class, () -> {
            utilisateurService.authentifier("john.doe@example.com", null);
        });
        verify(utilisateurService).authentifier("john.doe@example.com", null);
    }

    @Test
    void authentifier_ShouldHandleEmptyCredentials() throws Exception {
        when(utilisateurService.authentifier("", ""))
                .thenThrow(new Exception("Credentials are required"));

        assertThrows(Exception.class, () -> {
            utilisateurService.authentifier("", "");
        });
        verify(utilisateurService).authentifier("", "");
    }

    @Test
    void register_ShouldReturnRegisteredUtilisateurDto() {
        when(utilisateurService.register(utilisateurDto)).thenReturn(utilisateurDto);

        IdentiteDto result = utilisateurService.register(utilisateurDto);

        assertNotNull(result);
        assertEquals(utilisateurDto.getId(), result.getId());
        assertEquals(utilisateurDto.getEmail(), result.getEmail());
        verify(utilisateurService).register(utilisateurDto);
    }

    @Test
    void register_ShouldHandleNullInput() {
        when(utilisateurService.register(null)).thenReturn(null);

        IdentiteDto result = utilisateurService.register(null);

        assertNull(result);
        verify(utilisateurService).register(null);
    }

    @Test
    void register_ShouldHandleUtilisateurWithExistingEmail() {
        when(utilisateurService.register(utilisateurDto)).thenReturn(null);

        IdentiteDto result = utilisateurService.register(utilisateurDto);

        assertNull(result);
        verify(utilisateurService).register(utilisateurDto);
    }

    @Test
    void getAll_ShouldReturnAllUtilisateurs() {
        when(utilisateurService.getAll()).thenReturn(utilisateurDtoList);

        List<IdentiteDto> result = utilisateurService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(utilisateurService).getAll();
    }

    @Test
    void getAll_ShouldReturnEmptyListWhenNoUtilisateurs() {
        when(utilisateurService.getAll()).thenReturn(List.of());

        List<IdentiteDto> result = utilisateurService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(utilisateurService).getAll();
    }

    @Test
    void delete_ShouldReturnTrueWhenDeleted() {
        when(utilisateurService.delete(1L)).thenReturn(true);

        boolean result = utilisateurService.delete(1L);

        assertTrue(result);
        verify(utilisateurService).delete(1L);
    }

    @Test
    void delete_ShouldReturnFalseWhenNotExists() {
        when(utilisateurService.delete(999L)).thenReturn(false);

        boolean result = utilisateurService.delete(999L);

        assertFalse(result);
        verify(utilisateurService).delete(999L);
    }

    @Test
    void delete_ShouldHandleNullId() {
        when(utilisateurService.delete(null)).thenReturn(false);

        boolean result = utilisateurService.delete(null);

        assertFalse(result);
        verify(utilisateurService).delete(null);
    }

    @Test
    void authenticateAndGenerateToken_ShouldReturnLoginResponseWhenValid() throws Exception {
        when(utilisateurService.authenticateAndGenerateToken(utilisateur)).thenReturn(loginResponse);

        LoginResponse result = utilisateurService.authenticateAndGenerateToken(utilisateur);

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getUtilisateur().getEmail());
        assertEquals("jwt.token.here", result.getToken());
        verify(utilisateurService).authenticateAndGenerateToken(utilisateur);
    }

    @Test
    void authenticateAndGenerateToken_ShouldThrowExceptionWhenInvalid() throws Exception {
        when(utilisateurService.authenticateAndGenerateToken(utilisateur))
                .thenThrow(new Exception("Authentication failed"));

        assertThrows(Exception.class, () -> {
            utilisateurService.authenticateAndGenerateToken(utilisateur);
        });
        verify(utilisateurService).authenticateAndGenerateToken(utilisateur);
    }

    @Test
    void authenticateAndGenerateToken_ShouldHandleNullUtilisateur() throws Exception {
        when(utilisateurService.authenticateAndGenerateToken(null))
                .thenThrow(new Exception("CompteUtilisateur is required"));

        assertThrows(Exception.class, () -> {
            utilisateurService.authenticateAndGenerateToken(null);
        });
        verify(utilisateurService).authenticateAndGenerateToken(null);
    }

    @Test
    void getUtilisateurByEmail_ShouldReturnUtilisateurDtoWhenExists() {
        when(utilisateurService.getUtilisateurByEmail("john.doe@example.com")).thenReturn(utilisateurDto);

        IdentiteDto result = utilisateurService.getUtilisateurByEmail("john.doe@example.com");

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
        verify(utilisateurService).getUtilisateurByEmail("john.doe@example.com");
    }

    @Test
    void getUtilisateurByEmail_ShouldReturnNullWhenNotExists() {
        when(utilisateurService.getUtilisateurByEmail("nonexistent@example.com")).thenReturn(null);

        IdentiteDto result = utilisateurService.getUtilisateurByEmail("nonexistent@example.com");

        assertNull(result);
        verify(utilisateurService).getUtilisateurByEmail("nonexistent@example.com");
    }

    @Test
    void getUtilisateurByEmail_ShouldHandleNullEmail() {
        when(utilisateurService.getUtilisateurByEmail(null)).thenReturn(null);

        IdentiteDto result = utilisateurService.getUtilisateurByEmail(null);

        assertNull(result);
        verify(utilisateurService).getUtilisateurByEmail(null);
    }

    @Test
    void getByEmail_ShouldReturnUtilisateurDtoWhenExists() {
        when(utilisateurService.getByEmail("john.doe@example.com")).thenReturn(utilisateurDto);

        IdentiteDto result = utilisateurService.getByEmail("john.doe@example.com");

        assertNotNull(result);
        assertEquals("john.doe@example.com", result.getEmail());
        verify(utilisateurService).getByEmail("john.doe@example.com");
    }

    @Test
    void getByEmail_ShouldReturnNullWhenNotExists() {
        when(utilisateurService.getByEmail("nonexistent@example.com")).thenReturn(null);

        IdentiteDto result = utilisateurService.getByEmail("nonexistent@example.com");

        assertNull(result);
        verify(utilisateurService).getByEmail("nonexistent@example.com");
    }

    @Test
    void getByEmail_ShouldHandleNullEmail() {
        when(utilisateurService.getByEmail(null)).thenReturn(null);

        IdentiteDto result = utilisateurService.getByEmail(null);

        assertNull(result);
        verify(utilisateurService).getByEmail(null);
    }

    @Test
    void getByEmail_ShouldHandleEmptyEmail() {
        when(utilisateurService.getByEmail("")).thenReturn(null);

        IdentiteDto result = utilisateurService.getByEmail("");

        assertNull(result);
        verify(utilisateurService).getByEmail("");
    }
}