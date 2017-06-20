package org.ws13.howtos.vertx.eventbus.receiver;

import io.vertx.core.AbstractVerticle;

/**
 * @author ctranxuan
 */
public class ReplierWithFailure extends AbstractVerticle {
    private int counter = 0;

    @Override
    public void start() throws Exception {
        super.start();

        String address;
        address = config().getString("msg.address");

        System.out.printf("ReplierWithFailure.start for listening address %s\n", address);

        vertx.eventBus()
             .consumer(address, m -> {
                 System.out.printf("received %s from %s\n", m.body(), address);

                 counter = counter + 1;
                 if (counter < 3) {
                    m.fail(500, "failed to reply, sorry... ¯\\_(⊙︿⊙)_/¯");

                 } else {
                     counter = 0;
                     m.reply("World!!!");
                 }
             });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("ReplierWithFailure.stop");
    }
}
