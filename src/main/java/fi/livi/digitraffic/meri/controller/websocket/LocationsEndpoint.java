package fi.livi.digitraffic.meri.controller.websocket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.config.AisApplicationConfiguration;
import fi.livi.digitraffic.meri.model.ais.StatusMessage;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeature;

@ServerEndpoint(value = AisApplicationConfiguration.API_V1_BASE_PATH + AisApplicationConfiguration.API_PLAIN_WEBSOCKETS_PART_PATH
        + "/locations", encoders = {StatusEncoder.class, LocationEncoder.class})
@Component
public class LocationsEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(LocationsEndpoint.class);

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(final Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(final Session session) {
        sessions.remove(session);
    }

    @OnError
    public void onError(final Throwable t) {
        LOG.info("exception", t);
    }

    public static void sendMessage(final VesselLocationFeature message) {
        synchronized (sessions) {
            WebsocketStatistics.sentWebsocketStatistics(WebsocketStatistics.WebsocketType.LOCATIONS, sessions.size());

            WebsocketMessageSender.sendMessage(LOG, message, sessions);
        }
    }

    public static void sendStatus(final String status) {
        synchronized (sessions) {
            WebsocketMessageSender.sendMessage(LOG, new StatusMessage(status), sessions);
        }
    }
}
