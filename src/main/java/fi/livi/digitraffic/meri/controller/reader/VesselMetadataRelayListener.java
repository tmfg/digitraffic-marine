package fi.livi.digitraffic.meri.controller.reader;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.controller.AisLocker;
import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.controller.VesselSender;
import fi.livi.digitraffic.meri.controller.websocket.WebsocketStatistics;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselMessage;
import fi.livi.digitraffic.meri.service.ais.VesselMetadataService;
import fi.livi.digitraffic.meri.util.service.LockingService;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.websocketRead.enabled")
public class VesselMetadataRelayListener implements WebsocketListener {
    private final VesselMetadataService vesselMetadataService;
    private final VesselSender vesselSender;

    private final AisLocker aisLocker;

    public VesselMetadataRelayListener(final VesselMetadataService vesselMetadataService, final VesselSender vesselSender,
        final LockingService lockingService, final AisLocker aisLocker) {
        this.vesselMetadataService = vesselMetadataService;
        this.vesselSender = vesselSender;
        this.aisLocker = aisLocker;
    }

    @Override
    @Transactional
    public void receiveMessage(final String message) {
        final VesselMessage vm = MessageConverter.convertMetadata(message);

        if(vm.validate() && aisLocker.hasLockForAis()) {
            doLogAndSend(vm);
        }
    }

    private void doLogAndSend(final VesselMessage vm) {
        if(isAllowedMmsi(vm.vesselAttributes.mmsi)) {
            final VesselMetadata vessel = new VesselMetadata(vm.vesselAttributes);

            WebsocketStatistics.sentWebsocketStatistics(WebsocketStatistics.WebsocketType.METADATA, 1, 0);
            vesselSender.sendMetadataMessage(vessel);
        } else {
            WebsocketStatistics.sentWebsocketStatistics(WebsocketStatistics.WebsocketType.METADATA, 0, 1);
        }
    }

    private boolean isAllowedMmsi(int mmsi) {
        return vesselMetadataService.findAllowedMmsis().contains(mmsi);
    }

    @Override public void connectionStatus(final ReconnectingHandler.ConnectionStatus status) {
        // no need to do anything
    }
}