package fi.livi.digitraffic.meri.controller.reader;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.controller.VesselSender;
import fi.livi.digitraffic.meri.controller.websocket.WebsocketStatistics;
import fi.livi.digitraffic.meri.model.ais.AISMessage;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeature;
import fi.livi.digitraffic.meri.service.ais.VesselLocationConverter;
import fi.livi.digitraffic.meri.service.ais.VesselMetadataService;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.websocketRead.enabled")
public class VesselLocationRelayListener implements WebsocketListener {
    private final VesselSender vesselSender;
    private final VesselMetadataService vesselMetadataService;

    public VesselLocationRelayListener(final VesselSender vesselSender, final VesselMetadataService vesselMetadataService) {
        this.vesselSender = vesselSender;
        this.vesselMetadataService = vesselMetadataService;
    }

    @Override
    public void receiveMessage(final String message) {
        final AISMessage ais = MessageConverter.convertLocation(message);

        // Send only allowed mmsis to WebSocket, ship type 30 fishing boat filtered
        if (ais.validate() && isAllowedMmsi(ais.attributes.mmsi)) {
            final VesselLocationFeature feature = VesselLocationConverter.convert(ais);

            WebsocketStatistics.sentWebsocketStatistics(WebsocketStatistics.WebsocketType.LOCATIONS);
            vesselSender.sendLocationMessage(feature);
        }
    }

    private boolean isAllowedMmsi(final int mmsi) {
        return vesselMetadataService.findAllowedMmsis().contains(mmsi);
    }

    @Override
    public void connectionStatus(final ReconnectingHandler.ConnectionStatus status) {
        // no need to do anything
    }
}
