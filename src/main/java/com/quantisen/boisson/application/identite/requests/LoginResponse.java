package com.quantisen.boisson.application.identite.requests;

import com.quantisen.boisson.web.identite.dtos.IdentiteDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class LoginResponse {
    private IdentiteDto utilisateur;
    private String token;
}
