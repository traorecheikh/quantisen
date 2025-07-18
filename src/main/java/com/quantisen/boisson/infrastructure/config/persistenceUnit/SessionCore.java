package com.quantisen.boisson.infrastructure.config.persistenceUnit;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@Dependent
public class SessionCore {

    @Inject
    private EntityCore entityCore;

    @Produces
    @Dependent
    public EntityManager produceEntityManager() {
        return entityCore.getEmf().createEntityManager();
    }

    public void closeEntityManager(@Disposes EntityManager em) {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
