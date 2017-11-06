package fi.livi.digitraffic.meri.controller.reader;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class WebsocketReader {
    private final Logger log;

    private final String locationUrl;

    private volatile ClientManager clientManager = null;
    private volatile Session session = null;
    private volatile LocalDateTime lastMessageTime = null;

    private final List<WebsocketListener> listeners;

    private final AtomicBoolean running = new AtomicBoolean(true);

    private static final TemporalAmount IDLE_TIME_LIMIT = Duration.ofMinutes(1);

    public WebsocketReader(final String locationUrl, final List<WebsocketListener> listeners) {
        this.locationUrl = locationUrl;
        this.listeners = listeners;

        this.log = LoggerFactory.getLogger(getClass());
    }

    public void initialize() {
        if (StringUtils.isEmpty(locationUrl)) {
            log.info("Empty location, no reader started");
        } else {
            new Thread(() -> {
                try {
                    clientManager = initializeConnection();
                } catch (final Exception e) {
                    log.error("error", e);
                }
            }).start();
        }
    }

    private boolean isIdle() {
        return lastMessageTime != null && lastMessageTime.isBefore(LocalDateTime.now().minus(IDLE_TIME_LIMIT));
    }

    public void idleTimeout() {
        if(isIdle() && session != null && session.isOpen()) {
            log.error("Restarting connection to url={} because it's been idle since lastMessageTime={}", locationUrl, lastMessageTime);
            try {
                session.close();
            } catch (final Exception e) {
                log.error("error", e);
            }
        }
    }

    private ClientManager initializeConnection() throws URISyntaxException, IOException, DeploymentException {
        log.info("Initializing connection to url={} listenersCount={} listeners", locationUrl, listeners.size());

        final ReconnectingHandler handler = new ReconnectingHandler(locationUrl, listeners, log);
        final MessageHandler messageHandler = new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(final String message) {
                receiveMessage(message);
            }
        };

        final Endpoint endPoint = new Endpoint() {
            @Override
            public void onOpen(final Session session, final EndpointConfig endpointConfig) {
                WebsocketReader.this.session = session;
                handler.onOpen();

                // for some reason, this does NOT work with lambda or method reference
                session.addMessageHandler(messageHandler);
            }

            @Override
            public void onClose(final Session session, final CloseReason closeReason) {
                log.info("Connection to url={} closed because reason={}", locationUrl, closeReason);
                lastMessageTime = null;

                session.removeMessageHandler(messageHandler);
            }

            public void onError(final Session session, final Throwable thr) {
                log.error("Connection to url={} caused error message={}", locationUrl, thr.getMessage());
            }
        };

        final ClientManager client = ClientManager.createClient();

        client.getProperties().put(ClientProperties.RECONNECT_HANDLER, handler);
        client.connectToServer(endPoint, ClientEndpointConfig.Builder.create().build(), new URI(locationUrl));

        return client;
    }

    private void receiveMessage(final String message) {
        lastMessageTime = LocalDateTime.now();

        if (running.get()) {
            listeners.parallelStream().forEach(listener -> notifyListener(listener, message));
        } else {
            log.warn("Not handling messages received after shutdown hook");
        }
    }

    private void notifyListener(final WebsocketListener listener, final String message) {
        try {
            listener.receiveMessage(message);
        } catch(final Exception e) {
            log.error("exception for message " + message, e);
        }
    }

    public void destroy() {
        log.debug("destroy");
        running.set(false);
        if(clientManager != null) {
            clientManager.shutdown();
        }
    }
}
