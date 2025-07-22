package com.quantisen.boisson.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class QuantiSen extends Application {
    private static final Logger logger = LoggerFactory.getLogger(QuantiSen.class);
    public QuantiSen() {
        logger.info("QuantiSen application started");
//        // Send 500 logs to Loki
//        for (int i = 1; i <= 50; i++) {
//            logger.info("Loki test log #{}", i);
//        }
    }
}