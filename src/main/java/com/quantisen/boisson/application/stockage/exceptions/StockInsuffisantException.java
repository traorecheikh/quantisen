package com.quantisen.boisson.application.stockage.exceptions;

public class StockInsuffisantException extends RuntimeException {
    public StockInsuffisantException() {
        super("Stock insuffisant pour la sortie demandée");
    }
}

