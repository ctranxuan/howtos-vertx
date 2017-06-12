package org.ws13.howtos.vertx.discoveries.hello;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.AbstractServiceReference;

import java.util.Objects;

/**
 * @author ctranxuan
 */
public class HelloReference extends AbstractServiceReference<HelloService> {

    private final JsonObject conf;

    public HelloReference(final Vertx aVertx, final ServiceDiscovery aDiscovery,
                          final Record aRecord, final JsonObject aConfiguration) {
        super(aVertx, aDiscovery, aRecord);
        Objects.requireNonNull(aConfiguration);
        conf = aConfiguration;
    }

    @Override
    protected HelloService retrieve() {
        Boolean isLowerCase;
        isLowerCase = conf.getBoolean("lower-case", Boolean.TRUE);

        return new HelloServiceImpl(isLowerCase);
    }

    @Override
    protected void close() {
        // add your code here, if ever your service object needs cleanup
        super.close();
    }
}
