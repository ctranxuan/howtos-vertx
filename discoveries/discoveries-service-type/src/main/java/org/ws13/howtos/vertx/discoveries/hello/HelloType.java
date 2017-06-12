package org.ws13.howtos.vertx.discoveries.hello;

import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.spi.ServiceType;

import java.util.Objects;

import static io.vertx.servicediscovery.Record.ENDPOINT;

/**
 * @author ctranxuan
 */
public interface HelloType extends ServiceType {
    String TYPE = "hello-type";

    static Record createRecord(final String aName, final String aAddress, final JsonObject aMetadata) {
        Objects.requireNonNull(aName);
        Objects.requireNonNull(aAddress);

        JsonObject location;
        location = new JsonObject().put(ENDPOINT, aAddress);

        return createRecord(aName, location, aMetadata);
    }

    static Record createRecord(final String aName, final JsonObject aLocation, final JsonObject aMetadata) {
        Objects.requireNonNull(aName);
        Objects.requireNonNull(aLocation);

        Record record;
        record = new Record().setName(aName)
                             .setType(TYPE)
                             .setLocation(aLocation);

        if (aMetadata != null) {
            record.setMetadata(aMetadata);
        }

        return record;
    }
}
