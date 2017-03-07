package fi.livi.digitraffic.meri.controller.reader;

import java.util.List;

import javax.websocket.CloseReason;

import org.glassfish.tyrus.client.ClientManager;
import org.slf4j.Logger;

public class ReconnectingHandler extends ClientManager.ReconnectHandler {
    private final Logger log;

    private final List<WebsocketListener> listeners;

    private static final long MAX_WAIT_SECONDS = 120;

    private long failureCount = 0;

    public ReconnectingHandler(final List<WebsocketListener> listeners, final Logger log) {
        this.listeners = listeners;
        this.log= log;
    }

    public void onOpen() {
        log.info("connected");

        notifyStatus(ConnectionStatus.CONNECTED);

        failureCount = 0;
    }

    @Override
    public boolean onDisconnect(final CloseReason closeReason) {
        log.error("disconnect: {} {}", closeReason.getCloseCode(), closeReason.getReasonPhrase());

        notifyStatus(ConnectionStatus.DISCONNECTED);

        return true;
    }

    @Override
    public boolean onConnectFailure(final Exception exception) {
        log.error("connectFailure", exception);

        notifyStatus(ConnectionStatus.CONNECT_FAILURE);

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

    private void notifyStatus(final ConnectionStatus status) {
        listeners.parallelStream().forEach(listener -> notifyListener(listener, status));
    }

    private void notifyListener(final WebsocketListener listener, final ConnectionStatus status) {
        try {
            listener.connectionStatus(status);
        } catch(final Exception e) {
            log.error("exception for status " + status, e);
        }
    }


    public enum ConnectionStatus {
        STARTED, CONNECTED, DISCONNECTED, CONNECT_FAILURE
    }
}
