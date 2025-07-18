package com.quantisen.boisson.infrastructure.config.persistenceUnit;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@ApplicationScoped
@Named
public class EntityCore {
    private final EntityManagerFactory emf;

    public EntityCore() {
        emf = Persistence.createEntityManagerFactory("oulyPU");
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }
}
