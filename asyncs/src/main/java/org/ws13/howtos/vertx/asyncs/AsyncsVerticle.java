package org.ws13.howtos.vertx.asyncs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.ws13.howtos.vertx.asyncs.services.HelloService;

/**
 * @author ctranxuan
 */
public class AsyncsVerticle extends AbstractVerticle {

    private static class PrintHandler implements Handler<AsyncResult<String>> {

        @Override
        public void handle(final AsyncResult<String> aResult) {
            if (aResult.succeeded()) {
                System.out.printf("%s\n", aResult.result());

            } else {
                System.err.println("failed to print the result! :(");

            }
        }
    }
    @Override
    public void start() throws Exception {
        super.start();
        System.out.println("AsyncsVerticle.start");

        HelloService helloService;
        helloService = new HelloService();

        helloService.sayHello(new JsonObject().put("name", "Arthur Accroc"), new PrintHandler());
        helloService.sayHelloWithFuture(new JsonObject().put("name", "Ford Escort"), new PrintHandler());
        helloService.sayHelloWithFuture2(new JsonObject().put("name", "Trillian McMillan"), new PrintHandler());
        helloService.sayHelloWithRx(new JsonObject().put("name", "Marvin the Bot"), new PrintHandler());
        helloService.sayHelloWithRx2(new JsonObject().put("name", "Zappy Bibicy"), new PrintHandler());
        helloService.sayHelloWithRx3(new JsonObject().put("name", "Vorlon"), new PrintHandler());
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("AsyncsVerticle.stop");
    }
}
