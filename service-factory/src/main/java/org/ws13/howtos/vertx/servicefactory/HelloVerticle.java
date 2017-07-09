package org.ws13.howtos.vertx.servicefactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

import java.util.Objects;

/**
 * @author ctranxuan
 */
public class HelloVerticle extends AbstractVerticle {
    private final String hello;


    public HelloVerticle(final String aHello) {
        hello = Objects.requireNonNull(aHello);
    }

    @Override
    public void start() throws Exception {
        System.out.printf("HelloVerticle.start: %s\n", hello);
    }

    @Override
    public void stop() throws Exception {
        System.out.printf("HelloVerticle.stop: %s\n", hello);
    }
}
