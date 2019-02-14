package fi.livi.digitraffic.meri.controller.reader;

import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.model.ais.AISMessage;

// Poistetaan ei enaa tarpeellinen
// Talla on yritetty metsastaa viivetta
public class VesselLocationLoggingListener implements WebsocketListener {
    private static final Logger log = LoggerFactory.getLogger(VesselLocationLoggingListener.class);

    private Instant lastLogged = Instant.now();
    private Long maxDifference = null;

    @Override
    public void receiveMessage(final String message) {
        final AISMessage ais = MessageConverter.convertLocation(message);
        final Instant now = Instant.now();

        final long difference = now.toEpochMilli() - ais.attributes.timestampExternal;

        maxDifference = maxDifference == null || maxDifference < difference ? difference : maxDifference;

        if(lastLogged.isBefore(now.minus(1, MINUTES))) {
            log.info("VesselLocation maxAge={}", maxDifference);

            lastLogged = now;
            maxDifference = null;
        }
    }

    @Override
    public void connectionStatus(final ReconnectingHandler.ConnectionStatus status) {
        // do nothing
    }
}
