package com.quantisen.boisson.application.stockage.exceptions;

public class QuantiteDemandeeInvalideException extends RuntimeException {
    public QuantiteDemandeeInvalideException() {
        super("La quantité demandée doit être positive");
    }
}

