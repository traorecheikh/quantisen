package com.quantisen.boisson.application.identite.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRequest {
    @JsonProperty("ancienMotDePasse")
    private String ancienMotDePasse;
    @JsonProperty("nouveauMotDePasse")
    private String nouveauMotDePasse;
}
