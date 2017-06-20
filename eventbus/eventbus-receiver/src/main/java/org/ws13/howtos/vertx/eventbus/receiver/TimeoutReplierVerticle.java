package org.ws13.howtos.vertx.eventbus.receiver;

import io.vertx.core.AbstractVerticle;

/**
 * @author ctranxuan
 */
public class TimeoutReplierVerticle extends AbstractVerticle {
    private int counter = 0;

    @Override
    public void start() throws Exception {
        super.start();

        String address;
        address = config().getString("msg.address");

        Long sleepDuration;
        sleepDuration = config().getLong("sleep.duration");

        System.out.printf("TimeoutReplierVerticle.start for listening address %s with timeout %d ms\n", address, sleepDuration);

        vertx.eventBus()
             .consumer(address, m -> {
                 System.out.printf("received %s from %s\n", m.body(), address);

                 sleepTwice(sleepDuration);
                 m.reply("World!!!");
             });
    }

    private void sleepTwice(final Long aSleepDuration) {
        try {
            counter = counter + 1;

            if (counter == 3) {
                counter = 0;

            } else {
                System.out.printf("sleeping for the %d time(s)\n", counter);
                Thread.sleep(aSleepDuration);


            }

        } catch (InterruptedException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("TimeoutReplierVerticle.stop");
    }
}
