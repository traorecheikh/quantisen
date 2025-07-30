package com.quantisen.boisson.domaine.fournisseur.domainModel;

import com.quantisen.boisson.domaine.stockage.domainModel.Lot;
import com.quantisen.boisson.web.fournisseur.dtos.FournisseurDto;
import com.quantisen.boisson.domaine.boisson.domainModel.Boisson;
import com.quantisen.boisson.domaine.fournisseur.enums.StatutFournisseur;
import com.quantisen.boisson.web.stockage.dtos.LotDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "fournisseurs")
@ToString
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
    @OneToMany(mappedBy = "fournisseur", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    List<Lot> lots;
    private String createdAt;
    private String updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now().toString();
        this.statut = StatutFournisseur.EN_ATTENTE_VALIDATION;
        this.updatedAt = this.createdAt;
        if (this.lots != null) {
            for (Lot lot : this.lots) {
                lot.setFournisseur(this);
            }
        }else {
            this.lots = List.of();
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now().toString();
    }

    public FournisseurDto toDto() {
        return toDto(false);
    }

    /**
     * Converts this Fournisseur to a FournisseurDto. If shallow is true, nested objects are not fully converted to avoid recursion.
     */
    public FournisseurDto toDto(boolean shallow) {
        return FournisseurDto.builder()
                .id(this.id)
                .nom(this.nom)
                .contact(this.contact)
                .adresse(this.adresse)
                .statut(this.statut)
                .dateContrat(this.dateContrat)
                .lots((!shallow && this.lots != null) ? this.lots.stream().map(lot -> lot.toDto(true)).toList() : List.of())
                .dateResiliation(this.dateResiliation)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
