package fi.livi.digitraffic.meri.controller.reader;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.StringUtils;

import fi.livi.digitraffic.meri.model.Validatable;

public abstract class WebsocketReader<T extends Validatable> implements DisposableBean {
    private final Logger log;

    private final String locationUrl;

    private volatile ClientManager clientManager = null;

    public WebsocketReader(final String locationUrl) {
        this.locationUrl = locationUrl;
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
        log.debug("initializing connection to " + locationUrl);

        final ClientManager client = ClientManager.createClient();
        final ReconnectingHandler handler = new ReconnectingHandler(log);

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

    private void receiveMessage(final String s) {
        log.debug("receiveMessage " + s);

        final T msg = convert(s);

        try {
            if(msg.validate()) {
                handleMessage(msg);
            } else {
                log.debug("could not validate message", msg);
            }
        } catch(final Exception e) {
            log.error("exception for message " + s, e);
        }

    }

    @Override
    public void destroy() throws Exception {
        log.debug("destroy");

        if(clientManager != null) {
            clientManager.shutdown();
        }
    }

    protected abstract T convert(final String message);

    protected abstract void handleMessage(final T message);
}
