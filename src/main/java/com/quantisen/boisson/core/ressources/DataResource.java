package com.quantisen.boisson.core.ressources;

import com.quantisen.boisson.infrastructure.config.genesis.DataDispatcher;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/hello-world")
public class DataResource {
    @Inject
    private DataDispatcher dataDispatcher;

    @GET
    @Produces("text/plain")
    public String hello() {
        dataDispatcher.seed();
//        logger.debug("dddddddddddd");
        return "data seeded";
    }
}