package fi.livi.digitraffic.meri.controller.websocket;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.StatusMessage;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeature;

@ServerEndpoint(value = AisApplicationConfiguration.API_V1_BASE_PATH + AisApplicationConfiguration.API_PLAIN_WEBSOCKETS_PART_PATH + "/locations/{mmsi}",
                encoders = {StatusEncoder.class, LocationEncoder.class, MetadataEncoder.class})
@Component
public class VesselMMSIEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(VesselMMSIEndpoint.class);

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

    public static void sendLocationMessage(final VesselLocationFeature message) {
        synchronized (sessions) {
            final Set<Session> sessionSet = sessions.get(message.mmsi);

            if(sessionSet != null) {
                WebsocketStatistics.sentWebsocketStatistics(WebsocketStatistics.WebsocketType.VESSEL_LOCATION, sessionSet.size());

                WebsocketMessageSender.sendMessage(LOG, message, sessionSet);
            }
        }
    }

    public static void sendMetadataMessage(final VesselMetadata message) {
        synchronized (sessions) {
            final Set<Session> sessionSet = sessions.get(message.getMmsi());

            if(sessionSet != null) {
                WebsocketStatistics.sentWebsocketStatistics(WebsocketStatistics.WebsocketType.METADATA, sessionSet.size());

                WebsocketMessageSender.sendMessage(LOG, message, sessionSet);
            }
        }
    }

    public static void sendStatus(final String status) {
        synchronized (sessions) {
            final List<Session> sessionList = sessions.values().stream().flatMap(c -> c.stream()).collect(Collectors.toList());

            WebsocketMessageSender.sendMessage(LOG, new StatusMessage(status), sessionList);
        }
    }
}
