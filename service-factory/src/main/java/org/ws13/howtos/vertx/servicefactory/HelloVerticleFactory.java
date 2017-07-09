package org.ws13.howtos.vertx.servicefactory;

import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;

import java.util.Objects;

/**
 * @author ctranxuan
 */
public class HelloVerticleFactory implements VerticleFactory {
    private final String hello;

    public HelloVerticleFactory(final String aHello) {
        hello = Objects.requireNonNull(aHello);
    }

    @Override
    public String prefix() {
        return this.getClass().getPackage().getName() + ".hello-verticle-factory";
    }

    @Override
    public Verticle createVerticle(final String aVerticleName, final ClassLoader aClassLoader) throws Exception {
        System.out.printf("HelloVerticleFactory.createVerticle: %s\n", aVerticleName);
        return new HelloVerticle(hello);
    }
}
