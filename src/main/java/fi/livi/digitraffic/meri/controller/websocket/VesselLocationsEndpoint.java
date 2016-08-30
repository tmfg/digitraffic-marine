package fi.livi.digitraffic.meri.controller.websocket;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.config.AisApplicationConfiguration;
import fi.livi.digitraffic.meri.model.AISMessage;

@ServerEndpoint(value = AisApplicationConfiguration.API_V1_BASE_PATH + AisApplicationConfiguration.API_PLAIN_WEBSOCKETS_PART_PATH + "/locations/{mmsi}", encoders = LocationEncoder.class)
@Component
public class VesselLocationsEndpoint {
    private static Map<Integer, Set<Session>> sessions = Collections.synchronizedMap(new HashMap<>());

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
                for (final Session s : sessionSet) {
                    try {
                        s.getBasicRemote().sendObject(message);

                    } catch (final Exception ex) {
                        System.out.println("exception " + ex);
                    }
                }
            }
        }
    }
}
