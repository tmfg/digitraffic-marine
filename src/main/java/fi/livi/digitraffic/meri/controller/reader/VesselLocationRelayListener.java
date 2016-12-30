package fi.livi.digitraffic.meri.controller.reader;

import java.util.Collection;

import fi.livi.digitraffic.meri.controller.LocationSender;
import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.controller.websocket.LocationsEndpoint;
import fi.livi.digitraffic.meri.controller.websocket.VesselLocationsEndpoint;
import fi.livi.digitraffic.meri.model.ais.AISMessage;
import fi.livi.digitraffic.meri.service.ais.VesselMetadataService;

public class VesselLocationRelayListener implements WebsocketListener {
    private final LocationSender locationSender;
    private final VesselMetadataService vesselMetadataService;
    private Collection<Integer> allowedMmsisCached;
    private long allowedMmsisCacheRefreshedMs = 0L;

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
        } else {
            System.out.println("Fishing boat " + ais);
        }
    }

    private boolean isAllowedMmsi(int mmsi) {
        return getAllowedMmsisCached().contains(mmsi);
    }

    @Override
    public void connectionStatus(final ReconnectingHandler.ConnectionStatus status) {
        // no need to do anything
    }

    public Collection<Integer> getAllowedMmsisCached() {
        // fetch from db if more than minute is gone from last fetch
        if (allowedMmsisCached == null || allowedMmsisCacheRefreshedMs < (System.currentTimeMillis() - 1000 * 60)) {
            allowedMmsisCached = vesselMetadataService.findAllowedMmsis();
            allowedMmsisCacheRefreshedMs = System.currentTimeMillis();
        }
        return allowedMmsisCached;
    }
}
