package org.ws13.howtos.vertx.discoveries.hello;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;

/**
 * @author ctranxuan
 */
public class HelloPublisherVerticle extends AbstractVerticle {
    private ServiceDiscovery discovery;
    private Record helloServiceRecord;

    @Override
    public void start() throws Exception {
        super.start();
        System.out.println("HelloPublisherVerticle.start");
        discovery = ServiceDiscovery.create(vertx);

        Record helloRecord;
        helloRecord = HelloType.createRecord("helloooo", "the-hellooo-service-address", new JsonObject());

        discovery.publish(helloRecord, ar -> {
            if (ar.succeeded()) {
                helloServiceRecord = ar.result();
                System.out.println("Hello Service successfully published!");

            } else {
                System.err.println("Hello Service has not been published: hum, something went wrong... ಥ_ಥ");

            }
        });

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        discovery.unpublish(helloServiceRecord.getRegistration(),
                            ar -> {
                                if (ar.succeeded()) {
                                    System.out.println("Hello Service successfully unpublished!");

                                } else {
                                    System.err.println("Hum, there was something wrong with the unpublication of the Hello Service... ಠ_ಠ");
                                }
                            });
        discovery.close();
        System.out.println("HelloPublisherVerticle.stop");
    }
}
