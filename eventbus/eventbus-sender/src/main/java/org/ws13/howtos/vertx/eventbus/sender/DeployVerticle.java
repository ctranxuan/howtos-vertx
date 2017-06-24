package org.ws13.howtos.vertx.eventbus.sender;

import io.vertx.rxjava.core.AbstractVerticle;

/**
 * @author ctranxuan
 */
public class DeployVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        System.out.println("DeployVerticle.start");
        super.start();

//        vertx.deployVerticle(RetryWithHandlerVerticle.class.getName());
//        vertx.deployVerticle(RetryWithRxJavaVerticle.class.getName());
//        vertx.deployVerticle(PingVerticle.class.getName());
        vertx.deployVerticle(RxPingVerticle.class.getName());
    }


    @Override
    public void stop() throws Exception {
        super.stop();

        // very basic undeployment
        vertx.deploymentIDs()
             .forEach(id -> vertx.undeploy(id));

        System.out.println("DeployVerticle.stop");
    }
}
