package fi.livi.digitraffic.meri.controller.websocket;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.config.AisApplicationConfiguration;
import fi.livi.digitraffic.meri.controller.reader.ReconnectingHandler;
import fi.livi.digitraffic.meri.model.ais.AISMessage;
import fi.livi.digitraffic.meri.model.ais.StatusMessage;

@ServerEndpoint(value = AisApplicationConfiguration.API_V1_BASE_PATH + AisApplicationConfiguration.API_PLAIN_WEBSOCKETS_PART_PATH + "/locations/{mmsi}",
        encoders = {StatusEncoder.class, LocationEncoder.class})
@Component
public class VesselLocationsEndpoint extends WebsocketEndpoint{
    private static final Logger LOG = LoggerFactory.getLogger(VesselLocationsEndpoint.class);

    private static final Map<Integer, Set<Session>> sessions = Collections.synchronizedMap(new HashMap<>());

    @OnOpen
    public void onOpen(final Session session, @PathParam("mmsi") final Integer mmsi) {
        synchronized (sessions) {
            if(!sessions.containsKey(mmsi)) {
                sessions.put(mmsi, new HashSet<>());
            }

            sessions.get(mmsi).add(session);
        }
    }

    @OnClose
    public void onClose(final Session session, @PathParam("mmsi") final Integer mmsi) {
        synchronized (sessions) {
            sessions.get(mmsi).remove(session);
        }
    }

    public static void sendMessage(final AISMessage message) {
        synchronized (sessions) {
            final Set<Session> sessionSet = sessions.get(message.attributes.mmsi);

            if(sessionSet != null) {
                sendMessage(LOG, message, sessionSet);
            }
        }
    }

    public static void sendStatus(final ReconnectingHandler.ConnectionStatus status) {
        synchronized (sessions) {
            sendMessage(LOG, new StatusMessage(status.name()), sessions.values().stream().flatMap(c -> c.stream()).collect(Collectors.toList()));
        }
    }
}
