package com.quantisen.boisson.domaine.fournisseur.domainModel;

import com.quantisen.boisson.application.fournisseur.dtos.FournisseurDto;
import com.quantisen.boisson.domaine.boisson.domainModel.Boisson;
import com.quantisen.boisson.domaine.fournisseur.enums.StatutFournisseur;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "fournisseurs")
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 100)
    private String nom;
    @Size(max = 255)
    private String contact;
    @Size(max = 255)
    private String adresse;
    @Enumerated(EnumType.STRING)
    private StatutFournisseur statut;
    private String dateContrat;
    private String dateResiliation;
    @OneToMany(mappedBy = "fournisseur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Boisson> boissons;
    private String createdAt;
    private String updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now().toString();
        this.statut = StatutFournisseur.EN_ATTENTE_VALIDATION;
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now().toString();
    }

    public FournisseurDto toDto() {
        return FournisseurDto.builder()
                .id(this.id)
                .nom(this.nom)
                .contact(this.contact)
                .adresse(this.adresse)
                .statut(this.statut)
                .dateContrat(this.dateContrat)
                .dateResiliation(this.dateResiliation)
                .boissons(this.boissons.stream()
                        .map(Boisson::toDto)
                        .toList())
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}

