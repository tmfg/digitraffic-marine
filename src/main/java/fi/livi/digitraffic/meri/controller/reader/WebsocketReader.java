package fi.livi.digitraffic.meri.controller.reader;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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

public abstract class WebsocketReader<T> {
    private final Logger log;

    private final String locationUrl;

    public WebsocketReader(final String locationUrl) {
        this.locationUrl = locationUrl;
        this.log = LoggerFactory.getLogger(getClass());
    }

    public void initialize() {
        try {
            initializeConnection();
        } catch (final Exception e) {
            log.error("error", e);
        }
    }

    private void initializeConnection() throws URISyntaxException, IOException, DeploymentException {
        log.debug("initializing connection to " + locationUrl);

        final ClientManager client = ClientManager.createClient();

        client.getProperties().put(ClientProperties.RECONNECT_HANDLER, new ReconnectingHandler());
        client.connectToServer(new Endpoint() {
            @Override public void onOpen(final Session session, final EndpointConfig endpointConfig) {
                log.debug("connected");

                // for some reason, this does NOT work with lambda or method reference
                session.addMessageHandler(new MessageHandler.Whole<String>() {
                    @Override
                    public void onMessage(final String message) {
                        receiveMessage(message);
                    }
                });
            }
        }, ClientEndpointConfig.Builder.create().build(), new URI(locationUrl));
    }

    private void receiveMessage(final String s) {
        log.debug("receiveMessage " + s);

        final T msg = convert(s);

        try {
            handleMessage(msg);
        } catch(final Exception e) {
            log.error("exception", e);
        }

    }

    protected abstract T convert(final String message);

    protected abstract void handleMessage(final T message);

    private class ReconnectingHandler extends ClientManager.ReconnectHandler {
        @Override
        public boolean onDisconnect(final CloseReason closeReason) {
            log.error("disconnect");

            return true;
        }

        @Override
        public boolean onConnectFailure(final Exception exception) {
            log.error("connectFailure");

            return true;
        }
    }
}
