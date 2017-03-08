package fi.livi.digitraffic.meri.controller.reader;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.CloseReason;

import org.glassfish.tyrus.client.ClientManager;
import org.slf4j.Logger;

public class ReconnectingHandler extends ClientManager.ReconnectHandler {
    private final Logger log;

    private final String locationUrl; // for debugging

    private final List<WebsocketListener> listeners;

    private static final long MAX_WAIT_SECONDS = 120;

    private AtomicInteger failureCount = new AtomicInteger(0);

    public ReconnectingHandler(final String locationUrl, final List<WebsocketListener> listeners, final Logger log) {
        this.locationUrl = locationUrl;
        this.listeners = listeners;
        this.log= log;
    }

    public void onOpen() {
        log.info("connected {}", locationUrl);

        notifyStatus(ConnectionStatus.CONNECTED);

        failureCount.set(0);
    }

    @Override
    public boolean onDisconnect(final CloseReason closeReason) {
        log.error("disconnect {}: {} {}", locationUrl, closeReason.getCloseCode(), closeReason.getReasonPhrase());

        notifyStatus(ConnectionStatus.DISCONNECTED);

        return true;
    }

    @Override
    public boolean onConnectFailure(final Exception exception) {
        log.error("connectFailure " + locationUrl, exception);

        notifyStatus(ConnectionStatus.CONNECT_FAILURE);

        return true;
    }

    @Override
    public long getDelay() {
        return Math.min(MAX_WAIT_SECONDS, failureCount.getAndIncrement() * 1);
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
