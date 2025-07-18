package com.quantisen.boisson.domaine.stockage.domainModel;

import com.quantisen.boisson.application.stockage.dtos.LigneOperationDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "mouvement_lots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneOperation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "mouvement_id", nullable = false)
    private Mouvement mouvement;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lot_id", nullable = false)
    private Lot lot;

    @Min(1)
    @Column(nullable = false)
    private int quantite;

    public LigneOperationDto toDto(boolean includeMouvement) {
        LigneOperationDto.LigneOperationDtoBuilder builder = LigneOperationDto.builder()
                .id(this.id)
                .quantite(this.quantite)
                .lot(this.lot.toDto());
        if (includeMouvement && this.mouvement != null) {
            builder.mouvement(this.mouvement.toDto());
        }
        return builder.build();
    }


}
