package com.quantisen.boisson.domaine.boisson.domainModel;

import com.quantisen.boisson.application.boisson.dtos.BoissonDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "boissons")
public class Boisson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 50)
    private String nom;
    @Size(max = 255)
    private String description;
    @Min(5)
    private double prix;
    @Min(0)
    private double volume;
    @Size(max = 10)
    private String unite;
    @Min(0)
    private int seuil;
    private boolean isActive;
    private String createdAt;
    private String updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now().toString();
        this.isActive = true;
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now().toString();
    }

    public BoissonDto toDto() {
        return BoissonDto.builder()
                .id(this.id)
                .nom(this.nom)
                .description(this.description)
                .prix(this.prix)
                .volume(this.volume)
                .unite(this.unite)
                .seuil(this.seuil)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
