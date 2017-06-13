package org.ws13.howtos.vertx.discoveries.hello.consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import org.ws13.howtos.vertx.discoveries.hello.HelloService;
import org.ws13.howtos.vertx.discoveries.hello.HelloType;

/**
 * @author ctranxuan
 */
public class HelloConsumerVerticle extends AbstractVerticle {

    private ServiceDiscovery discovery;

    @Override
    public void start() throws Exception {
        System.out.println("HelloConsumerVerticle.start");
        super.start();

        discovery = ServiceDiscovery.create(vertx);
        discovery.getRecord(r -> HelloType.TYPE.equals(r.getType()), ar -> {
            if (ar.succeeded() && ar.result() != null) {
                ServiceReference reference;
                reference = discovery.getReference(ar.result());

                HelloService helloService = reference.get();
                helloService.sayHello("Scotty");

                // don't forget to release the reference
                reference.release();
            } else {
                System.out.println("Failed to get a non-null Hello Service... (⊙_☉)");
            }
        });


        discovery.getRecord(new JsonObject().put("name", "helloooo"), ar -> {
            if (ar.succeeded() && ar.result() != null) {
                ServiceReference reference;
                reference = discovery
                        .getReferenceWithConfiguration(ar.result(), new JsonObject().put("lower-case", false));

                HelloService helloService = reference.get();
                helloService.sayHello("Mr Spock");

                // don't forget to release the reference
                reference.release();
            } else {
                System.out.println("Failed to get a non-null configured Hello Service... (⊙_☉)");
            }
        });
    }

    @Override
    public void stop() throws Exception {
        System.out.println("HelloConsumerVerticle.stop");
        discovery.close();
        super.stop();
    }
}
