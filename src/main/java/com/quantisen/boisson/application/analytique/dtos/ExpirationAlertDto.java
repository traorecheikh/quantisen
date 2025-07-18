package com.quantisen.boisson.application.analytique.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpirationAlertDto {
    private String lotCode;
    private String beverageName;
    private String expirationDate;
    private int quantity;
    private boolean expired;
    private boolean expiringSoon;
}