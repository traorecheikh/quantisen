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
public class IdentiteCredentialsRequest {
    @JsonProperty("email")
    private String email;
    @JsonProperty("motDePasse")
    private String motDePasse;
}
