package fi.livi.digitraffic.meri.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Component
public class VesselLocationReader {
    private static final Logger LOG = LoggerFactory.getLogger(VesselLocationReader.class);

    private final String serverAddress;

    private final LocationSender locationSender;

    @Autowired
    public VesselLocationReader(@Value("${ais.server.address}") final String serverAddress, final LocationSender locationSender) {
        this.locationSender = locationSender;
        this.serverAddress = serverAddress;

        initializeConnection();
    }

    private void initializeConnection() {
        LOG.debug("initializing connection to " + serverAddress);

        final StandardWebSocketClient client = new StandardWebSocketClient();

        client.doHandshake(new VesselLocationMessageHandler(), serverAddress);
    }

    private class VesselLocationMessageHandler implements WebSocketHandler {
        @Override
        public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
            LOG.debug("afterConnectionEstablished");
        }

        @Override
        public void handleMessage(final WebSocketSession session, final WebSocketMessage<?> message) throws Exception {
            LOG.debug("handleMessage " + message.getPayload());

            locationSender.sendMessage((String) message.getPayload());
        }

        @Override
        public void handleTransportError(final WebSocketSession session, final Throwable exception) throws Exception {
            LOG.debug("handleTransportError", exception);
        }

        @Override
        public void afterConnectionClosed(final WebSocketSession session, final CloseStatus closeStatus) throws Exception {
            LOG.debug("afterConnectionClosed " + closeStatus);

            initializeConnection();
        }

        @Override
        public boolean supportsPartialMessages() {
            return false;
        }
    }
}
