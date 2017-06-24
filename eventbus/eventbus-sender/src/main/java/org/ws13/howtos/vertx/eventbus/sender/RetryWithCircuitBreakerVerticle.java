package org.ws13.howtos.vertx.eventbus.sender;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.AbstractVerticle;

/**
 * @author ctranxuan
 */
public class RetryWithCircuitBreakerVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        System.out.println("RetryWithCircuitBreakerVerticle.start");
        CircuitBreaker breaker;
        breaker = CircuitBreaker.create("my-circuit-breaker", vertx,
                new CircuitBreakerOptions()
                        .setMaxRetries(3)
                        .setMaxFailures(3) // number of failure before opening the circuit
//                        .setTimeout(8500) // consider a failure if the operation does not succeed in time
//                        .setFallbackOnFailure(false) // do we call the fallback on failure
//                        .setResetTimeout(10000) // time spent in open state before attempting to re-try
        );

        breaker.execute(fut ->
                vertx.eventBus()
                        .<String>send("hello.handler.failure.circuit-breaker.retry", "Hello", ar -> {
                            if (ar.succeeded()) {
                                fut.complete(ar.result().body());

                            } else {
                                System.out.printf("circuit-breaker scenario with handler: received \"%s\"\n", ar.cause().getMessage());
                                fut.fail(ar.cause().getMessage());

                            }
                        }))
                .setHandler(ar -> {
                    // Get the operation result.
                    if (ar.succeeded()) {
                        System.out.printf("circuit-breaker scenario with handler: received \"%s\"\n", ar.result());

                    } else {
                        ar.cause().printStackTrace();
                    }
                });
    }

    @Override
    public void stop() throws Exception {
        System.out.println("RetryWithCircuitBreakerVerticle.stop");
    }
}
