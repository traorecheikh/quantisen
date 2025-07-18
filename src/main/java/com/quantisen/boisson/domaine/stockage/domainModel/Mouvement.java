package com.quantisen.boisson.domaine.stockage.domainModel;

import com.quantisen.boisson.application.stockage.dtos.LigneOperationDto;
import com.quantisen.boisson.application.stockage.dtos.MouvementDto;
import com.quantisen.boisson.domaine.identite.domainModel.CompteUtilisateur;
import com.quantisen.boisson.domaine.stockage.enums.TypeAjustement;
import com.quantisen.boisson.domaine.stockage.enums.TypeMouvement;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Table(name = "mouvements2")
public class Mouvement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(updatable = false, nullable = false)
    private TypeMouvement type;
    @NotNull
    @Column(updatable = false, nullable = false)
    private String dateMouvement;
    @Min(1)
    @Column(updatable = false, nullable = false, columnDefinition = "integer CHECK (quantite > 0)")
    private int quantite;
    @OneToMany(mappedBy = "mouvement", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<LigneOperation> ligneOperations = new ArrayList<>();
    @NotNull
    @ManyToOne
    @JoinColumn(name = "utilisateur_id", updatable = false, nullable = false)
    private CompteUtilisateur utilisateur;
    @Enumerated(EnumType.STRING)
    @Column(name = "type_ajustement", updatable = false)
    private TypeAjustement typeAjustement;
    @Column(name = "raison", columnDefinition = "varchar(255) CHECK (LENGTH(raison) <= 255)")
    private String raison;

    @PrePersist
    public void prePersist() {
        this.dateMouvement = LocalDateTime.now().toString();
    }

    @PreUpdate
    public void preUpdate() {
        throw new UnsupportedOperationException("Update operation is not supported for Mouvement entity.");
    }

    @PreRemove
    public void preDestroy() {
        throw new UnsupportedOperationException("Delete operation is not supported for Mouvement entity.");
    }

    public MouvementDto toDto() {
        MouvementDto dto = new MouvementDto();
        dto.setId(this.id);
        dto.setType(this.type);
        dto.setDateMouvement(this.dateMouvement);
        dto.setQuantite(this.quantite);
        dto.setUtilisateur(this.utilisateur.toDto());
        dto.setTypeAjustement(this.typeAjustement);
        dto.setRaison(this.raison);
        if (this.ligneOperations != null) {
            List<LigneOperationDto> ligneDtos = new ArrayList<>();
            for (LigneOperation ligne : this.ligneOperations) {
                ligneDtos.add(ligne.toDto(false)); // Prevent infinite recursion
            }
            dto.setLigneOperation(ligneDtos);
        }
        return dto;
    }
}