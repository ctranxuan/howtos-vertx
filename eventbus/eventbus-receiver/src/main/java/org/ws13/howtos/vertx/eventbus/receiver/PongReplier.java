package org.ws13.howtos.vertx.eventbus.receiver;

import java.util.Objects;

/**
 * @author ctranxuan
 */
class PongReplier {

   String reply(final String aResponse) {
        Objects.requireNonNull(aResponse);

        String reply;
        switch (aResponse) {
            case "ping":
                reply = "pong";
                break;

            case "pang":
                reply = "pung";
                break;

            case "peng":
                reply = "pyng";
                break;

            default:
                reply = "the ball went out of the table";
                break;
        }

        return reply;
    }
}
