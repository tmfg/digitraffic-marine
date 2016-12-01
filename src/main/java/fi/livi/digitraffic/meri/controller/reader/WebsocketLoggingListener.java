package fi.livi.digitraffic.meri.controller.reader;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.livi.digitraffic.meri.controller.websocket.LocationsEndpoint;
import fi.livi.digitraffic.meri.controller.websocket.VesselLocationsEndpoint;

public class WebsocketLoggingListener implements WebsocketListener {
    private final AtomicInteger messageCount = new AtomicInteger();
    private final AtomicReference<ReconnectingHandler.ConnectionStatus> connectionStatus = new AtomicReference<>();
    private final String domain;

    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private static final Logger log = LoggerFactory.getLogger(WebsocketLoggingListener.class);

    public WebsocketLoggingListener(final String domain, final boolean sendConnectionStatus) {
        executor.scheduleAtFixedRate(this::logMessageCount, 0, 1, TimeUnit.MINUTES);

        if(sendConnectionStatus) {
            executor.scheduleAtFixedRate(this::notifyStatus, 0, 10, TimeUnit.SECONDS);
        }

        this.domain = domain;
    }

    private void notifyStatus() {
        final ReconnectingHandler.ConnectionStatus status = connectionStatus.get();

        LocationsEndpoint.sendStatus(status);
        VesselLocationsEndpoint.sendStatus(status);
    }

    private void logMessageCount() {
        final int callCount = messageCount.getAndSet(0);
        final ReconnectingHandler.ConnectionStatus status = connectionStatus.get();

        log.info(String.format("Websocket statistics for %s : %d : %s",
                domain,
                callCount,
                status == null ? "UNDEFINED" : status.name()));
    }

    @Override
    public void receiveMessage(final String message) {
        messageCount.incrementAndGet();
    }

    @Override public void connectionStatus(final ReconnectingHandler.ConnectionStatus status) {
        connectionStatus.set(status);
    }
}
