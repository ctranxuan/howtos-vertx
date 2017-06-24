package org.ws13.howtos.vertx.eventbus.sender;

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.Message;
import rx.functions.Action1;

/**
 * @author ctranxuan
 */
public class RxPingVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        System.out.println("RxPingVerticle.start");

        vertx.eventBus()
                .<String>rxSend("ping.pong.rxtopic", "ping")
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

        vertx.eventBus()
                .<String>rxSend("ping.pong.rxtopic2", "ping")
                .subscribe(new RxReplySubscriber());
    }

    @Override
    public void stop() throws Exception {
        System.out.println("RxPingVerticle.stop");
    }

    private static class RxReplySubscriber implements Action1<Message<String>> {
        private final PingReplier replier = new PingReplier();

        @Override
        public void call(final Message<String> aMessage) {
            String response;
            response = aMessage.body();

            System.out.printf("[%s] received %s\n", RxPingVerticle.class.getName(), response);
            String reply;
            reply = replier.reply(response);

            aMessage.<String>rxReply(reply).subscribe(this);
        }
    }
}
