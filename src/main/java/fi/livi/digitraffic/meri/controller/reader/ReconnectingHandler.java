package fi.livi.digitraffic.meri.controller.reader;

import javax.websocket.CloseReason;

import org.glassfish.tyrus.client.ClientManager;
import org.slf4j.Logger;

public class ReconnectingHandler extends ClientManager.ReconnectHandler {
    private final Logger log;

    private static final long MAX_WAIT_SECONDS = 120;

    private long failureCount = 0;

    public ReconnectingHandler(final Logger log) {
        this.log= log;
    }

    public void onOpen() {
        log.debug("connected");

        failureCount = 0;
    }

    @Override
    public boolean onDisconnect(final CloseReason closeReason) {
        log.error("disconnect");

        return true;
    }

    @Override
    public boolean onConnectFailure(final Exception exception) {
        log.error("connectFailure");

        sleep();

        return true;
    }

    private void sleep() {
        // wait up to maximum seconds, increasing 6 seconds for every failure
        final long millis = Math.min(MAX_WAIT_SECONDS, failureCount * 6) * 1000;

        try {
            Thread.sleep(millis);
        } catch (final InterruptedException e) {
            log.error("error while sleeping", e);
        }

        failureCount++;
    }
}
