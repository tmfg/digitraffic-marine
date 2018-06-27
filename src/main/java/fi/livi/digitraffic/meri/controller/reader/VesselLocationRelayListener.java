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
import fi.livi.digitraffic.meri.util.service.LockingService;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.websocketRead.enabled")
public class VesselLocationRelayListener implements WebsocketListener {
    private final VesselSender vesselSender;
    private final VesselMetadataService vesselMetadataService;

    private final LockingService lockingService;

    public VesselLocationRelayListener(final VesselSender vesselSender, final VesselMetadataService vesselMetadataService,
        final LockingService lockingService) {
        this.vesselSender = vesselSender;
        this.vesselMetadataService = vesselMetadataService;
        this.lockingService = lockingService;
    }

    @Override
    public void receiveMessage(final String message) {
        final AISMessage ais = MessageConverter.convertLocation(message);

        if (ais.validate() && lockingService.hasLockForAis()) {
            doLogAndSend(ais);
        }
    }

    private void doLogAndSend(final AISMessage ais) {
        if (isAllowedMmsi(ais.attributes.mmsi)) {
            final VesselLocationFeature feature = VesselLocationConverter.convert(ais);

            WebsocketStatistics.sentWebsocketStatistics(WebsocketStatistics.WebsocketType.LOCATIONS, 1, 0);
            vesselSender.sendLocationMessage(feature);
        } else {
            WebsocketStatistics.sentWebsocketStatistics(WebsocketStatistics.WebsocketType.LOCATIONS, 0, 1);
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
