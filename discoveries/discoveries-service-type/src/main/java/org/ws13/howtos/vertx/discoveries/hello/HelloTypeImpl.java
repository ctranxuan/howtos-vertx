package org.ws13.howtos.vertx.discoveries.hello;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;

/**
 * @author ctranxuan
 */
public class HelloTypeImpl implements HelloType {
    @Override
    public String name() {
        return TYPE;
    }

    @Override
    public ServiceReference get(final Vertx aVertx,
                                final ServiceDiscovery aDiscovery,
                                final Record aRecord,
                                final JsonObject aConfiguration) {
        return new HelloReference(aVertx, aDiscovery, aRecord, aConfiguration);
    }
}
