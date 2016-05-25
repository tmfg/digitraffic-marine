package fi.livi.digitraffic.meri.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import fi.livi.digitraffic.meri.model.AISMessage;

public abstract class VesselLocationReader {
    private final Logger log;

    private final String serverAddress;

    private WebSocketConnectionManager webSocketConnectionManager= null;

    public VesselLocationReader(final String serverAddress) {
        this.serverAddress = serverAddress;

        this.log = LoggerFactory.getLogger(getClass());

        initializeConnection();
    }

    private void initializeConnection() {
        log.debug("initializing connection to " + serverAddress);

        final StandardWebSocketClient client = new StandardWebSocketClient();
        webSocketConnectionManager = new WebSocketConnectionManager(client, new VesselLocationMessageHandler(), serverAddress);

        webSocketConnectionManager.start();
    }

    private class VesselLocationMessageHandler implements WebSocketHandler {
        @Override
        public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
            log.debug("afterConnectionEstablished");
        }

        @Override
        public void handleMessage(final WebSocketSession session, final WebSocketMessage<?> message) throws Exception {
            log.debug("handleMessage " + message.getPayload());

            final AISMessage ais = MessageConverter.convert((String) message.getPayload());

            VesselLocationReader.this.handleMessage(ais);
        }

        @Override
        public void handleTransportError(final WebSocketSession session, final Throwable exception) throws Exception {
            log.debug("handleTransportError", exception);
        }

        @Override
        public void afterConnectionClosed(final WebSocketSession session, final CloseStatus closeStatus) throws Exception {
            log.debug("afterConnectionClosed " + closeStatus);

            initializeConnection();
        }

        @Override
        public boolean supportsPartialMessages() {
            return false;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected abstract void handleMessage(final AISMessage message);
}
