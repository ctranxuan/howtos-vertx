package org.ws13.howtos.vertx.eventbus.receiver.rxjava;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;

/**
 * @author streamdataio
 */
public class DeployVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        System.out.println("DeployVerticle.start");
        super.start();

        vertx.deployVerticle(RxPongVerticle.class, new DeploymentOptions());
    }
}
