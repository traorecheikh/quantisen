package com.quantisen.boisson.application.identite.requests;

import com.quantisen.boisson.application.identite.dtos.IdentiteDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private IdentiteDto utilisateur;
    private String token;
}
