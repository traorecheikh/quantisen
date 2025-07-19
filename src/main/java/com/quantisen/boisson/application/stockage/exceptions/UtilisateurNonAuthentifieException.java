package com.quantisen.boisson.application.stockage.exceptions;

public class UtilisateurNonAuthentifieException extends RuntimeException {
    public UtilisateurNonAuthentifieException() {
        super("L'utilisateur doit être authentifié");
    }
}

