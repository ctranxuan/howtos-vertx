package org.ws13.howtos.vertx.asyncs.services;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.rx.java.RxHelper;
import rx.Completable;

/**
 * @author ctranxuan
 */
public class WhisperService {

    public void whisper(String aName, Handler<AsyncResult<Void>> aHandler) {
        sayHello(aName, hello -> {
            if (hello.succeeded()) {
                sayGoodBye(aName, bye -> {
                   if (bye.succeeded()) {
                       sayTchuss(tchuss -> {
                           // we could do
                           if (tchuss.succeeded()) {
                               aHandler.handle(Future.succeededFuture());

                           } else {
                               aHandler.handle(Future.failedFuture(tchuss.cause()));

                           }

                       });

                   } else {
                       aHandler.handle(Future.failedFuture(bye.cause()));

                   }

                });

            } else {
                aHandler.handle(Future.failedFuture(hello.cause()));

            }
        });
    }

    public void whisperWithFuture(final String aName, final Handler<AsyncResult<Void>> aHandler) {
        Future<Void> future;
        future = Future.future();
        future.setHandler(aHandler);

        Future<Void> hello;
        hello = Future.future();
        sayHello(aName, hello);

        hello
            .compose(v -> {
                Future<Void> bye;
                bye = Future.future();

                sayGoodBye(aName, bye);
                return bye;
            })
            .compose(v -> {
                sayTchuss(future.completer());

            }, future);
    }

    public void whisperWithRx(final String aName, final Handler<AsyncResult<Void>> aHandler) {
        rxSayHello(aName)
            .andThen(rxSayGoodBye(aName))
            .andThen(rxSayTchuss())
            .subscribe(RxHelper.toSubscriber(aHandler));
    }

    private void sayHello(final String aName, final Handler<AsyncResult<Void>> aHandler) {
        System.out.printf("Hello %s!\n", aName);
        aHandler.handle(Future.succeededFuture());
    }

    private Completable rxSayHello(final String aName) {
        return Completable.fromAction(() -> System.out.printf("Hello %s!\n", aName));
    }

    private void sayGoodBye(final String aName, final Handler<AsyncResult<Void>> aHandler) {
        System.out.printf("  Good bye %s!\n", aName);
        aHandler.handle(Future.succeededFuture());
    }

    private Completable rxSayGoodBye(final String aName) {
        return Completable.fromAction(() -> System.out.printf("  Good bye %s!\n", aName));
    }

    private void sayTchuss(final Handler<AsyncResult<Void>> aHandler) {
        System.out.println("    Tchuss!!!");
        aHandler.handle(Future.succeededFuture());
    }

    private Completable rxSayTchuss() {
        return Completable.fromAction(() -> System.out.println("    Tchuss!!!"));
    }
}
