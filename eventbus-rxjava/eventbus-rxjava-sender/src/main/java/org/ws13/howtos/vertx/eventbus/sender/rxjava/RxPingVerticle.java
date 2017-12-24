package org.ws13.howtos.vertx.eventbus.sender.rxjava;

import io.vertx.reactivex.core.AbstractVerticle;

/**
 * @author streamdataio
 */
public class RxPingVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        System.out.println("RxPingVerticle.start");
        super.start();

        vertx.eventBus()
                .<String>rxSend("ping.pong.rx", "ping")
                .flatMap(m -> {
                    System.out.printf("[%s] received %s\n", RxPingVerticle.class.getName(), m.body());
                    return m.rxReply("pang");
                })
                .flatMap(m -> {
                    System.out.printf("[%s] received %s\n", RxPingVerticle.class.getName(), m.body());
                    return m.rxReply("peng");
                })
                .subscribe(m -> System.out.printf("[%s] received %s\n", RxPingVerticle.class.getName(), m.body()),
                           Throwable::printStackTrace);

    }
}
