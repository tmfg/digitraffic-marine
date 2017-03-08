package fi.livi.digitraffic.meri.controller.reader;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.websocket.ClientEndpointConfig;
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

    private final List<WebsocketListener> listeners;

    private final AtomicBoolean running = new AtomicBoolean(true);

    public WebsocketReader(final String locationUrl, final List<WebsocketListener> listeners) {
        this.locationUrl = locationUrl;
        this.listeners = listeners;

        this.log = LoggerFactory.getLogger(getClass());
    }

    public void initialize() {
        if(StringUtils.isEmpty(locationUrl)) {
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

    private ClientManager initializeConnection() throws URISyntaxException, IOException, DeploymentException {
        log.info("Initializing connection to {} {} listeners", locationUrl, listeners.size());

        final ClientManager client = ClientManager.createClient();
        final ReconnectingHandler handler = new ReconnectingHandler(locationUrl, listeners, log);

        client.getProperties().put(ClientProperties.RECONNECT_HANDLER, handler);
        client.connectToServer(new Endpoint() {
            @Override public void onOpen(final Session session, final EndpointConfig endpointConfig) {
                handler.onOpen();

                // for some reason, this does NOT work with lambda or method reference
                session.addMessageHandler(new MessageHandler.Whole<String>() {
                    @Override
                    public void onMessage(final String message) {
                        receiveMessage(message);
                    }
                });
            }
        }, ClientEndpointConfig.Builder.create().build(), new URI(locationUrl));

        return client;
    }

    private void receiveMessage(final String message) {
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
