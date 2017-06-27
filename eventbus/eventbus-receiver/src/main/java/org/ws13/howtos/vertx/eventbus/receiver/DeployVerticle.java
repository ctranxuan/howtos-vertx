package org.ws13.howtos.vertx.eventbus.receiver;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;

/**
 * @author ctranxuan
 */
public class DeployVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        System.out.println("DeployVerticle.start");
        super.start();

        JsonObject handlerConfig;
        handlerConfig = new JsonObject()
                .put("msg.address", "hello.handler.timeout.retry")
                .put("sleep.duration", 2000L);

        vertx.deployVerticle(TimeoutReplierVerticle.class.getName(), new DeploymentOptions().setConfig(handlerConfig));

        JsonObject rxConfig;
        rxConfig = new JsonObject()
                .put("msg.address", "hello.rx.timeout.retry")
                .put("sleep.duration", 2000L);

        vertx.deployVerticle(TimeoutReplierVerticle.class.getName(), new DeploymentOptions().setConfig(rxConfig));

        vertx.deployVerticle(ReplierWithFailureVerticle.class.getName(),
                             new DeploymentOptions()
                                    .setConfig(new JsonObject().put("msg.address", "hello.handler.failure.retry")));

        vertx.deployVerticle(ReplierWithFailureVerticle.class.getName(),
                             new DeploymentOptions()
                                    .setConfig(new JsonObject().put("msg.address", "hello.rx.failure.retry")));

        vertx.deployVerticle(ReplierWithFailureVerticle.class.getName(),
                new DeploymentOptions()
                        .setConfig(new JsonObject().put("msg.address", "hello.handler.failure.circuit-breaker.retry")));

//        vertx.deployVerticle(PongVerticle.class.getName());
//        vertx.deployVerticle(RxPongVerticle.class.getName());
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
