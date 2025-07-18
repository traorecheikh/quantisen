package com.quantisen.boisson.infrastructure.config.genesis;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
@Named
public class DataDispatcher {

    @Inject
    GenesisLoader loader;

    public void seed() {
        loader.init();
    }
}