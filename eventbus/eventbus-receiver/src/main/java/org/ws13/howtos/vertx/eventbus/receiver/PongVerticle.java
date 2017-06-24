package org.ws13.howtos.vertx.eventbus.receiver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

/**
 * @author ctranxuan
 */
public class PongVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        System.out.println("PongVerticle.start");
        vertx.eventBus()
             .<String>consumer("ping.pong.topic", m -> {
                 System.out.printf("[%s] received %s\n", PongVerticle.class.getName(), m.body());
                 m.reply("pong", ar -> {
                     if (ar.succeeded()) {
                         System.out.printf("[%s] received %s\n", PongVerticle.class.getName(), ar.result().body());
                         ar.result().reply("pung", ar2 -> {
                             if (ar2.succeeded()) {
                                 System.out.printf("[%s] received %s\n", PongVerticle.class.getName(), ar2.result().body());
                                 ar2.result().reply("pyng");

                             } else {
                                 ar2.cause().printStackTrace();

                             }
                         });

                     } else {
                         ar.cause().printStackTrace();

                     }
                 });
             });

        vertx.eventBus()
             .<String>consumer("ping.pong.topic2", m -> {
                 System.out.printf("[%s] received %s\n", PongVerticle.class.getName(), m.body());
                 m.reply("pong", new PongReplyHandler());

             });
    }

    @Override
    public void stop() throws Exception {
        System.out.println("PongVerticle.stop");
    }

    private static class PongReplyHandler implements Handler<AsyncResult<Message<String>>> {
        private final PongReplier replier = new PongReplier();

        @Override
        public void handle(final AsyncResult<Message<String>> aResult) {
            if (aResult.succeeded()) {
                Message<String> message;
                message = aResult.result();

                String response;
                response = message.body();
                System.out.printf("[%s] received %s\n", PongVerticle.class.getName(), response);

                String reply;
                reply = replier.reply(response);

                message.reply(reply, this);

            } else {
                aResult.cause().printStackTrace();
            }
        }
    }
}
