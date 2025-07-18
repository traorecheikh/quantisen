package com.quantisen.boisson.core.ressources;

import com.quantisen.boisson.core.QuantiSen;
import com.quantisen.boisson.infrastructure.config.genesis.DataDispatcher;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/hello-world")
public class DataResource {
    @Inject
    private DataDispatcher dataDispatcher;
    Logger logger = LoggerFactory.getLogger(DataResource.class);

    @GET
    @Produces("text/plain")
    public String hello() {
//        dataDispatcher.seed();
//        logger.debug("dddddddddddd");
        return "data seeded";
    }
}