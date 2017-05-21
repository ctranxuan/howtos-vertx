package org.ws13.howtos.vertx.asyncs.services;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.rx.java.RxHelper;
import io.vertx.rx.java.SingleOnSubscribeAdapter;
import rx.Single;

import java.util.function.Consumer;

/**
 * @author ctranxuan
 */
public class HelloService {

    public void sayHello(JsonObject aName, Handler<AsyncResult<String>> aHandler) {
        String name;
        name = aName.getString("name");

        concatWithHello(name, hello -> {
            if (hello.succeeded()) {
                concatWithExclamation(hello.result(), exclamation -> {
                    if (exclamation.succeeded()) {
                        concatWithISay(exclamation.result(), aHandler);

                    } else {
                        aHandler.handle(Future.failedFuture(hello.cause()));

                    }
                });

            } else {
                aHandler.handle(Future.failedFuture(hello.cause()));

            }
        });
    }

    public void sayHelloWithFuture(JsonObject aName, Handler<AsyncResult<String>> aHandler) {
        String name;
        name = aName.getString("name");

        Future<String> future;
        future = Future.future();
        future.setHandler(aHandler);

        Future<String> hello;
        hello = Future.future();
        concatWithHello(name, hello);

        hello
            .compose(v -> {
                Future<String> exclamation;
                exclamation = Future.future();

                concatWithExclamation(v, exclamation.completer());
                return exclamation;

            })
            .compose(v -> {
                concatWithISay(v, future.completer());
            }, future);
    }

    public void sayHelloWithFuture2(JsonObject aName, Handler<AsyncResult<String>> aHandler) {
        // Probably sayHelloWithFuture() better since this one sticks to vertx.io doc about async sequence?!?
        String name;
        name = aName.getString("name");

        Future<String> hello;
        hello = Future.future();
        concatWithHello(name, hello);

        hello
            .compose(v -> {
                Future<String> exclamation;
                exclamation = Future.future();

                concatWithExclamation(v, exclamation.completer());
                return exclamation;

            })
            .compose(v -> {
                Future<String> future;
                future = Future.future();
                future.setHandler(aHandler);

                concatWithISay(v, future);
                return future;
            });
    }

    public void sayHelloWithRx(JsonObject aName, Handler<AsyncResult<String>> aHandler) {
        Single.just(aName)
              .map(n -> aName.getString("name"))
              .flatMap(name -> rxConcatWithHello(name))
              .flatMap(hello -> rxConcatWithExclamation(hello))
              .flatMap(exclamation -> rxConcatWithISay(exclamation))
              .subscribe(RxHelper.toSubscriber(aHandler));

        // Version with method reference
//        Single.just(aName)
//              .map(n -> aName.getString("name"))
//                .flatMap(this::rxConcatWithHello)
//                .flatMap(this::rxConcatWithExclamation)
//                .flatMap(this::rxConcatWithISay)
//                .subscribe(RxHelper.toSubscriber(aHandler));
    }

    public void sayHelloWithRx2(JsonObject aName, Handler<AsyncResult<String>> aHandler) {
        Single.just(aName)
              .map(n -> aName.getString("name"))
              .flatMap(name -> Single.<String>create(new SingleOnSubscribeAdapter<>(fut -> concatWithHello(name, fut))))
              .flatMap(hello -> Single.<String>create(new SingleOnSubscribeAdapter<>(fut -> concatWithExclamation(hello, fut))))
              .flatMap(exclamation -> Single.<String>create(new SingleOnSubscribeAdapter<>(fut -> concatWithISay(exclamation, fut))))
              .subscribe(RxHelper.toSubscriber(aHandler));
    }

    public void sayHelloWithRx3(JsonObject aName, Handler<AsyncResult<String>> aHandler) {
        Single.just(aName)
              .map(n -> aName.getString("name"))
              .flatMap(name -> RxFactory.<String>singleCreate(fut -> concatWithHello(name, fut)))
              .flatMap(hello -> RxFactory.<String>singleCreate(fut -> concatWithExclamation(hello, fut)))
              .flatMap(exclamation -> RxFactory.<String>singleCreate(fut -> concatWithISay(exclamation, fut)))
              .subscribe(RxHelper.toSubscriber(aHandler));
    }

    private void concatWithHello(String aName, Handler<AsyncResult<String>> aHandler) {
        aHandler.handle(Future.succeededFuture("Hello " + aName));

    }

    private Single<String> rxConcatWithHello(final String aName) {
        return Single.just("Hello " + aName);
    }

    private void concatWithExclamation(final String aHello, final Handler<AsyncResult<String>> aHandler) {
        aHandler.handle(Future.succeededFuture(aHello + "!"));

    }

    private Single<String> rxConcatWithExclamation(final String aHello) {
        return Single.just(aHello + "!");
    }

    private void concatWithISay(final String aString, final Handler<AsyncResult<String>> aHandler) {
        aHandler.handle(Future.succeededFuture("I say: \"" + aString + "\""));

    }

    private Single<String> rxConcatWithISay(final String aExclamation) {
        return Single.just("I say: \"" + aExclamation + "\"");
    }

    private static <T> Single<T> rxCreate(Consumer<Handler<AsyncResult<T>>> aConsumer) {
        return Single.<T>create(new SingleOnSubscribeAdapter<>(aConsumer));
    }

    private static class RxFactory {
        public static <T> Single<T> singleCreate(Consumer<Handler<AsyncResult<T>>> aConsumer) {
            return Single.create(new SingleOnSubscribeAdapter<>(aConsumer));
        }
    }
}
