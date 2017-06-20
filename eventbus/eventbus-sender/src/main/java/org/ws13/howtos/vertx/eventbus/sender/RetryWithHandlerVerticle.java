package org.ws13.howtos.vertx.eventbus.sender;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;

/**
 * @author ctranxuan
 */
public class RetryWithHandlerVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        System.out.println("RetryWithHandlerVerticle.start");
        super.start();

        DeliveryOptions deliveryOptions;
        deliveryOptions = new DeliveryOptions().setSendTimeout(1500);

        vertx.eventBus()
             .send("hello.handler.timeout.retry", "Hello", deliveryOptions, new Handler<AsyncResult<Message<String>>>() {
                 private int count = 1;

                 @Override
                 public void handle(final AsyncResult<Message<String>> aResult) {
                     if (aResult.succeeded()) {
                         System.out.printf("timeout scenario with handler: received \"%s\"\n", aResult.result().body());

                     } else if (count < 3) {
                         System.out.printf("timeout scenario with handler: retry count %d, received error \"%s\"\n",
                                           count, aResult.cause().getMessage());
                         vertx.eventBus().send("hello.handler.timeout.retry", "Hello", deliveryOptions, this);
                         count = count + 1;

                     } else {
                         aResult.cause().printStackTrace();

                     }
                 }
             });

        vertx.eventBus()
             .send("hello.handler.failure.retry", "Hello", new Handler<AsyncResult<Message<String>>>() {
                 private int count = 1;

                 @Override
                 public void handle(final AsyncResult<Message<String>> aResult) {
                     if (aResult.succeeded()) {
                         System.out.printf("failure scenario with handler: received \"%s\"\n", aResult.result().body());

                     } else if (count < 3) {
                         System.out.printf("failure scenario with handler: retry count %d, received error \"%s\"\n", count, aResult.cause().getMessage());
                         vertx.eventBus().send("hello.handler.failure.retry", "Hello", this);
                         count = count + 1;

                     } else {
                         aResult.cause().printStackTrace();

                     }
                 }
             });

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("RetryWithHandlerVerticle.stop");
    }
}
