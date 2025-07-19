package com.quantisen.boisson.application.stockage.exceptions;

public class BoissonIdInvalideException extends RuntimeException {
    public BoissonIdInvalideException() {
        super("L'ID de la boisson ne peut pas être nul");
    }
}

