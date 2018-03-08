package fi.livi.digitraffic.meri.controller.reader;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.controller.websocket.WebsocketStatistics;
import fi.livi.digitraffic.meri.model.ais.AISMessage;

public class VesselLocationLoggingListener extends WebsocketLoggingListener {
    private static final Logger log = LoggerFactory.getLogger(VesselLocationLoggingListener.class);

    private Instant lastLogged = Instant.now();
    private Long maxDifference = null;

    public VesselLocationLoggingListener(final WebsocketStatistics.WebsocketType type) {
        super(type);
    }

    @Override
    public void receiveMessage(final String message) {
        super.receiveMessage(message);

        final AISMessage ais = MessageConverter.convertLocation(message);
        final Instant now = Instant.now();

        final long difference = now.toEpochMilli() - ais.attributes.timestampExternal;

        maxDifference = maxDifference == null || maxDifference < difference ? difference : maxDifference;

        if(maxDifference != null && lastLogged.isBefore(now.minus(1, MINUTES))) {
            log.info("VesselLocation maxAge={}", maxDifference);

            lastLogged = now;
            maxDifference = null;
        }
    }
}
