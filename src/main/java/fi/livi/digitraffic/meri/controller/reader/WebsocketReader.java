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

import org.apache.commons.lang3.time.StopWatch;
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
    private static final TemporalAmount ON_OPEN_TIME_LIMIT = Duration.ofMinutes(3);

    public WebsocketReader(final String locationUrl, final List<WebsocketListener> listeners) {
        this.locationUrl = locationUrl;
        this.listeners = listeners;

        this.log = LoggerFactory.getLogger(getClass());
    }

    public void initialize() {
        if (StringUtils.isEmpty(locationUrl)) {
            log.info("method=initialize Empty location, no reader started");
        } else {
            new Thread(() -> {
                try {
                    clientManager = initializeConnection();
                } catch (final IOException | DeploymentException | URISyntaxException e) {
                    log.error("method=initialize locationUrl=" + locationUrl, e);
                }
            }).start();
        }
    }

    private boolean isIdle() {
        return lastMessageTime != null && lastMessageTime.isBefore(LocalDateTime.now().minus(IDLE_TIME_LIMIT));
    }

    public void idleTimeout() {
        if(isIdle() && session != null && session.isOpen()) {
            log.warn("Restarting connection to url={} because it's been idle since lastMessageTime={}", locationUrl, lastMessageTime);
            try {
                session.close();
            } catch (final IOException e) {
                log.error("method=idleTimeout locationUrl=" + locationUrl, e);
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
                log.info("method=onOpen Connection to url={} opened", locationUrl);
                WebsocketReader.this.session = session;
                handler.onOpen();

                lastMessageTime = LocalDateTime.now().plus(ON_OPEN_TIME_LIMIT);

                // for some reason, this does NOT work with lambda or method reference
                session.addMessageHandler(messageHandler);
            }

            @Override
            public void onClose(final Session session, final CloseReason closeReason) {
                log.info("method=onClose Connection to url={} closed because reason={}", locationUrl, closeReason.getReasonPhrase().replaceAll("\\s", "_"));
                lastMessageTime = null;

                session.removeMessageHandler(messageHandler);
            }

            public void onError(final Session session, final Throwable thr) {
                log.info("method=onError Connection to url={} caused error errorMessage={}", locationUrl, thr.getMessage().replaceAll("\\s", "_"));
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
            listeners.stream().forEach(listener -> trySend(listener, message));
        } else {
            log.warn("Not handling messages received after shutdown hook");
        }
    }

    private void trySend(final WebsocketListener listener, final String message) {
        try {
            final StopWatch sw = StopWatch.createStarted();

            listener.receiveMessage(message);

            log.info("{} took {} ms", listener.getClass().getName(), sw.getTime());
        } catch(final Exception e) {
            log.error("exception from listener", e);
        }
    }

    public void destroy() {
        running.set(false);
        if(clientManager != null) {
            clientManager.shutdown();
        }
    }
}
