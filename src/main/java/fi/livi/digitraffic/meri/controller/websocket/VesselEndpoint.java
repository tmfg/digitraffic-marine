package fi.livi.digitraffic.meri.controller.websocket;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
import fi.livi.digitraffic.meri.controller.websocket.dto.StatusMessageDto;
import fi.livi.digitraffic.meri.controller.websocket.dto.VesselLocationFeatureDto;
import fi.livi.digitraffic.meri.controller.websocket.dto.VesselMetadataDto;
import fi.livi.digitraffic.meri.controller.websocket.encoder.StatusMessageDtoEncoder;
import fi.livi.digitraffic.meri.controller.websocket.encoder.VesselLocationDtoEncoder;
import fi.livi.digitraffic.meri.controller.websocket.encoder.VesselMetadataDtoEncoder;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.StatusMessage;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeature;

@ServerEndpoint(value = AisApplicationConfiguration.API_V1_BASE_PATH + AisApplicationConfiguration.API_PLAIN_WEBSOCKETS_PART_PATH
                + "/locations",
                encoders = { StatusMessageDtoEncoder.class, VesselMetadataDtoEncoder.class, VesselLocationDtoEncoder.class })
@Component
public class VesselEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(VesselEndpoint.class);

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    private static final List<String> connectionClosedMessages = Arrays.asList("Broken pipe", "Connection reset by peer", "connection was aborted");

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
        final String message = t.getCause().getMessage();

        if (t instanceof IOException && connectionClosedMessages.stream().anyMatch(m -> message.contains(m))) {
            LOG.info("Established connection was aborted by client. Message: {}", t.getCause().getMessage());
        } else {
            LOG.error("Exception", t);
        }
    }

    public static void sendLocationMessage(final VesselLocationFeature vesselLocation) {
        final VesselLocationFeatureDto message = new VesselLocationFeatureDto(vesselLocation);
        synchronized (sessions) {
            WebsocketStatistics.sentWebsocketStatistics(WebsocketStatistics.WebsocketType.LOCATIONS, sessions.size());

            WebsocketMessageSender.sendMessage(LOG, message, sessions);
        }
    }

    public static void sendMetadataMessage(final VesselMetadata vessel) {
        final VesselMetadataDto message = new VesselMetadataDto(vessel);
        synchronized (sessions) {
            WebsocketStatistics.sentWebsocketStatistics(WebsocketStatistics.WebsocketType.METADATA, sessions.size());

            WebsocketMessageSender.sendMessage(LOG, message, sessions);
        }
    }

    public static void sendStatus(final String status) {
        final StatusMessageDto message = new StatusMessageDto(new StatusMessage(status));
        synchronized (sessions) {
            WebsocketMessageSender.sendMessage(LOG, message, sessions);
        }
    }
}
