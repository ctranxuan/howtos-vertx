package org.ws13.howtos.vertx.servicefactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;

/**
 * @author ctranxuan
 */
public class HelloDeployVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        System.out.println("HelloDeployVerticle.start");

        HelloVerticleFactory factory = new HelloVerticleFactory("Hello World!!!");
        vertx.registerVerticleFactory(factory);

        vertx.deployVerticle(factory.prefix() + ":" + HelloVerticle.class.getName(),
                             new DeploymentOptions().setInstances(2));
    }

    @Override
    public void stop() throws Exception {
        System.out.println("HelloDeployVerticle.stop");
    }
}
