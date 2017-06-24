package org.ws13.howtos.vertx.eventbus.receiver;

import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.Message;
import rx.functions.Action1;

/**
 * @author ctranxuan
 */
public class RxPongVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        System.out.println("RxPongVerticle.start");

        vertx.eventBus()
             .consumer("ping.pong.rxtopic", m -> {
                 System.out.printf("[%s] received %s\n", RxPongVerticle.class.getName(), m.body());

                 m.rxReply("pong")
                  .flatMap(m2 -> {
                     System.out.printf("[%s] received %s\n", RxPongVerticle.class.getName(), m2.body());
                     return m2.rxReply("pung");
                  })
                  .flatMap(m3 -> {
                     System.out.printf("[%s] received %s\n", RxPongVerticle.class.getName(), m3.body());
                     return m3.rxReply("pyng");
                  })
                 .subscribe();
             });

        vertx.eventBus()
             .consumer("ping.pong.rxtopic2",
                        m -> m.<String>rxReply("pong").subscribe(new RxPongSubscriber()));
    }

    @Override
    public void stop() throws Exception {
        System.out.println("RxPongVerticle.stop");
    }

    private static class RxPongSubscriber implements Action1<Message<String>> {
        private final PongReplier replier = new PongReplier();

        @Override
        public void call(final Message<String> aMessage) {
            String response;
            response = aMessage.body();
            System.out.printf("[%s] received %s\n", RxPongVerticle.class.getName(), response);

            String reply;
            reply = replier.reply(response);
            aMessage.<String>rxReply(reply).subscribe(this);
        }
    }
}
