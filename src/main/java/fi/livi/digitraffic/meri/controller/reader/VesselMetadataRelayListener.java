package fi.livi.digitraffic.meri.controller.reader;

import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.controller.VesselSender;
import fi.livi.digitraffic.meri.controller.websocket.VesselEndpoint;
import fi.livi.digitraffic.meri.controller.websocket.VesselMMSIEndpoint;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselMessage;
import fi.livi.digitraffic.meri.service.ais.VesselMetadataService;

public class VesselMetadataRelayListener implements WebsocketListener {
    private final VesselMetadataService vesselMetadataService;
    private final VesselSender vesselSender;

    public VesselMetadataRelayListener(final VesselMetadataService vesselMetadataService,
                                       final VesselSender vesselSender) {
        this.vesselMetadataService = vesselMetadataService;
        this.vesselSender = vesselSender;
    }

    @Override
    @Transactional
    public void receiveMessage(final String message) {
        final VesselMessage vm = MessageConverter.convertMetadata(message);

        if(vm.validate() && isAllowedMmsi(vm.vesselAttributes.mmsi)) {
            VesselMetadata vessel = new VesselMetadata(vm.vesselAttributes);

            vesselSender.sendMetadataMessage(vessel);
            VesselEndpoint.sendMetadataMessage(vessel);
            VesselMMSIEndpoint.sendMetadataMessage(vessel);
        }
    }

    private boolean isAllowedMmsi(int mmsi) {
        return vesselMetadataService.findAllowedMmsis().contains(mmsi);
    }

    @Override public void connectionStatus(final ReconnectingHandler.ConnectionStatus status) {
        // no need to do anything
    }
}