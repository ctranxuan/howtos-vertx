package org.ws13.howtos.vertx.eventbus.receiver.rxjava;

import io.vertx.reactivex.core.AbstractVerticle;

/**
 * @author streamdataio
 */
public class RxPongVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        System.out.println("RxPongVerticle.start");
        super.start();

        vertx.eventBus()
             .consumer("ping.pong.rx")
             .toFlowable()
             .flatMap(m -> {
                 System.out.printf("[%s] received %s\n", RxPongVerticle.class.getName(), m.body());
                 return m.rxReply("pong").toFlowable();
             })
             .flatMap(m -> {
                 System.out.printf("[%s] received %s\n", RxPongVerticle.class.getName(), m.body());
                 return m.rxReply("pung").toFlowable();
             })
             .flatMap(m -> {
                 System.out.printf("[%s] received %s\n", RxPongVerticle.class.getName(), m.body());
                 return m.rxReply("pyng").toFlowable();
             })
             .subscribe()
             ;
    }
}
