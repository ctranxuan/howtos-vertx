package org.ws13.howtos.vertx.asyncs;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import org.ws13.howtos.vertx.asyncs.services.HelloService;
import org.ws13.howtos.vertx.asyncs.services.WhisperService;

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

    private static class SayEndHandler implements Handler<AsyncResult<Void>> {

        @Override
        public void handle(final AsyncResult<Void> aResult) {
            if (aResult.succeeded()) {
                System.out.printf("        That's the end, folks!\n");

            } else {
                System.err.println("That ended bad, folks! Sorry :(");

            }
        }
    }

    @Override
    public void start() throws Exception {
        super.start();
        System.out.println("AsyncsVerticle.start");

        HelloService helloService;
        helloService = new HelloService();

        PrintHandler printHandler;
        printHandler = new PrintHandler();

        helloService.sayHello(new JsonObject().put("name", "Arthur Accroc"), printHandler);
        helloService.sayHelloWithFuture(new JsonObject().put("name", "Ford Escort"), printHandler);
        helloService.sayHelloWithFuture2(new JsonObject().put("name", "Trillian McMillan"), printHandler);
        helloService.sayHelloWithRx(new JsonObject().put("name", "Marvin the Bot"), printHandler);
        helloService.sayHelloWithRx2(new JsonObject().put("name", "Zappy Bibicy"), printHandler);
        helloService.sayHelloWithRx3(new JsonObject().put("name", "Vorlon"), printHandler);

        WhisperService whisperService;
        whisperService = new WhisperService();

        SayEndHandler sayEndHandler;
        sayEndHandler = new SayEndHandler();

        whisperService.whisper("Scotty", sayEndHandler);
        whisperService.whisperWithFuture("Chekov", sayEndHandler);
        whisperService.whisperWithRx("Sulu", sayEndHandler);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("AsyncsVerticle.stop");
    }
}
