package com.quantisen.boisson.domaine.stockage.domainModel;

import com.quantisen.boisson.application.stockage.dtos.LotDto;
import com.quantisen.boisson.domaine.boisson.domainModel.Boisson;
import com.quantisen.boisson.domaine.fournisseur.domainModel.Fournisseur;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "lots2")
public class Lot {
    @OneToOne(optional = false)
    @JoinColumn(name = "mouvement_id", updatable = false)
    Mouvement mouvementEntree;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(length = 50, unique = true, nullable = false, updatable = false)
    private String numeroLot;
    @Min(1)
    @Column(nullable = false, updatable = false)
    private int quantiteInitiale;
    @Min(0)
    @Column(nullable = false)
    private int quantiteActuelle;
    @Column(nullable = false)
    private boolean vendable;
    @Column(nullable = true)
    private String fournisseurString;
    @Column(name = "date_entree", updatable = false)
    private String dateEntree;
    @Column(name = "date_peremption", updatable = false)
    private String datePeremption;
    @OneToMany(mappedBy = "lot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LigneOperation> ligneOperations = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "boisson_id")
    private Boisson boisson;
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @PrePersist
    private void createdAt() {
        this.dateEntree = LocalDateTime.now().toString();
        this.quantiteActuelle = quantiteInitiale;
        this.vendable = true;
    }

    @PreUpdate
    private void validateQuantities() {
        if (quantiteActuelle > quantiteInitiale) {
            throw new RuntimeException("quantite actuelle ne peut pas etre superieure a quantite initiale");
        }
    }

    public LotDto toDto() {
        return LotDto.builder()
                .id(this.id)
                .numeroLot(this.numeroLot)
                .quantiteInitiale(this.quantiteInitiale)
                .quantiteActuelle(this.quantiteActuelle)
                .fournisseur(fournisseur == null ? null : fournisseur.toDto())
                .fournisseurString(this.fournisseurString)
                .dateEntree(this.dateEntree)
                .datePeremption(this.datePeremption)
                .vendable(this.vendable)
                .boisson(this.boisson.toDto())
                .build();
    }
}

