package org.ws13.howtos.vertx.eventbus.sender;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

/**
 * @author ctranxuan
 */
public class PingVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        System.out.println("PingVerticle.start");

        vertx.eventBus()
             .<String>send("ping.pong.topic", "ping", ar -> {
                 if (ar.succeeded()) {
                     Message<String> message;
                     message = ar.result();

                     System.out.printf("[%s] received message: %s\n", PingVerticle.class.getName(), message.body());
                     message.reply("pang", ar2 -> {
                         if (ar2.succeeded()) {
                             System.out.printf("[%s] received message: %s\n", PingVerticle.class.getName(), ar2.result().body());
                             ar2.result().reply("peng", ar3 -> {
                                 if (ar3.succeeded()) {
                                     System.out.printf("[%s] received message: %s\n", PingVerticle.class.getName(), ar3.result().body());

                                 } else {
                                     ar3.cause().printStackTrace();

                                 }
                             });

                         } else {
                             ar2.cause().printStackTrace();

                         }
                     });


                 } else {
                     ar.cause().printStackTrace();

                 }
             });

        vertx.eventBus()
             .send("ping.pong.topic2", "ping", new PingReplyHandler());
    }

    @Override
    public void stop() throws Exception {
        System.out.println("PingVerticle.stop");
    }

    private static class PingReplyHandler implements Handler<AsyncResult<Message<String>>> {
        private final PingReplier replier = new PingReplier();

        @Override
        public void handle(final AsyncResult<Message<String>> aResult) {
            if (aResult.succeeded()) {
                Message<String> message;
                message = aResult.result();

                String response;
                response = message.body();
                System.out.printf("[%s] received %s\n", PingVerticle.class.getName(), response);

                String reply;
                reply = replier.reply(response);

                message.reply(reply, this);

            } else {
                aResult.cause().printStackTrace();

            }
        }
    }
}
