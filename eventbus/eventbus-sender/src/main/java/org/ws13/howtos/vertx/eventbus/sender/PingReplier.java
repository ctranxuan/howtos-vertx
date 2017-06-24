package org.ws13.howtos.vertx.eventbus.sender;

import java.util.Objects;

/**
 * @author ctranxuan
 */
final class PingReplier {

    public String reply(String aResponse) {
        Objects.requireNonNull(aResponse);

        String reply;
        switch (aResponse) {
            case "pong":
                reply = "pang";
                break;

            case "pung":
                reply = "peng";
                break;

            case "pyng":
                reply = "ping";
                break;

            default:
                reply = "the ball went out of the table";
                break;
        }

        return reply;
    }
}
