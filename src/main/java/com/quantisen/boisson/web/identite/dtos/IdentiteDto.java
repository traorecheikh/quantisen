package com.quantisen.boisson.web.identite.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.quantisen.boisson.domaine.identite.domainModel.Role;
import com.quantisen.boisson.domaine.identite.domainModel.CompteUtilisateur;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdentiteDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("role")
    private Role role;

    @JsonProperty("motDePasse")
    private String motDePasse;

    @JsonProperty("isActive")
    private boolean isActive;

    @JsonProperty("isFirstLogin")
    private boolean isFirstLogin;

    @JsonProperty("createdAt")
    private String createdAt;

    @JsonProperty("updatedAt")
    private String updatedAt;

    public CompteUtilisateur toEntity(boolean includePassword) {
        if (!includePassword) {
            return CompteUtilisateur.builder()
                    .id(this.id)
                    .email(this.email)
                    .firstName(this.firstName)
                    .lastName(this.lastName)
                    .role(this.role)
                    .isActive(this.isActive)
                    .isFirstLogin(this.isFirstLogin)
                    .createdAt(this.createdAt)
                    .updatedAt(this.updatedAt)
                    .build();
        }
        return CompteUtilisateur.builder()
                .id(this.id)
                .email(this.email)
                .role(this.role)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .isFirstLogin(this.isFirstLogin)
                .motDePasse(this.motDePasse)
                .isActive(this.isActive)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
