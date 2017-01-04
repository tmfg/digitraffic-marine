package fi.livi.digitraffic.meri.controller.reader;

import fi.livi.digitraffic.meri.controller.LocationSender;
import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.controller.websocket.LocationsEndpoint;
import fi.livi.digitraffic.meri.controller.websocket.VesselLocationsEndpoint;
import fi.livi.digitraffic.meri.model.ais.AISMessage;
import fi.livi.digitraffic.meri.service.ais.VesselMetadataService;

public class VesselLocationRelayListener implements WebsocketListener {
    private final LocationSender locationSender;
    private final VesselMetadataService vesselMetadataService;

    public VesselLocationRelayListener(final LocationSender locationSender,
                                       final VesselMetadataService vesselMetadataService) {
        this.locationSender = locationSender;
        this.vesselMetadataService = vesselMetadataService;
    }

    @Override
    public void receiveMessage(final String message) {
        final AISMessage ais = MessageConverter.convertLocation(message);

        // Send only allowed mmsis to WebSocket, ship type 30 fishing boat filtered
        if (ais.validate() && isAllowedMmsi(ais.attributes.mmsi)) {
            locationSender.sendMessage(ais);
            LocationsEndpoint.sendMessage(ais);
            VesselLocationsEndpoint.sendMessage(ais);
        }
    }

    private boolean isAllowedMmsi(int mmsi) {
        return vesselMetadataService.findAllowedMmsis().contains(mmsi);
    }

    @Override
    public void connectionStatus(final ReconnectingHandler.ConnectionStatus status) {
        // no need to do anything
    }
}
