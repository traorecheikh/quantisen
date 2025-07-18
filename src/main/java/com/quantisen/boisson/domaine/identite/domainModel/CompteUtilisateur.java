package com.quantisen.boisson.domaine.identite.domainModel;

import com.quantisen.boisson.application.identite.dtos.IdentiteDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NamedQueries({
        @NamedQuery(name = "Utilisateur.findByEmail", query = "SELECT u FROM CompteUtilisateur u WHERE u.email = :email"),
        @NamedQuery(name = "Utilisateur.findAll", query = "SELECT u FROM CompteUtilisateur u")
})
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "utilisateurs")
@ToString
public class CompteUtilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(unique = true, updatable = false)
    private String email;
    private String motDePasse;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean isFirstLogin;
    private boolean isActive;
    private String createdAt;
    private String updatedAt;

    @PrePersist
    public void prePersist() {
        String now = LocalDateTime.now().toString();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        this.updatedAt = now;
        isFirstLogin = true;
        isActive = true;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now().toString();
    }

    public IdentiteDto toDto() {
        return IdentiteDto.builder()
                .id(this.id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .email(this.email)
                .role(this.role)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
