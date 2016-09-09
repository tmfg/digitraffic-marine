package fi.livi.digitraffic.meri.controller.reader;

import fi.livi.digitraffic.meri.controller.LocationSender;
import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.controller.websocket.LocationsEndpoint;
import fi.livi.digitraffic.meri.controller.websocket.VesselLocationsEndpoint;
import fi.livi.digitraffic.meri.model.AISMessage;

public class VesselLocationRelayListener implements WebsocketListener {
    private final LocationSender locationSender;

    public VesselLocationRelayListener(final LocationSender locationSender) {
        this.locationSender = locationSender;
    }

    @Override
    public void receiveMessage(final String message) {
        final AISMessage ais = MessageConverter.convertLocation(message);

        if(ais.validate()) {
            locationSender.sendMessage(ais);
            LocationsEndpoint.sendMessage(ais);
            VesselLocationsEndpoint.sendMessage(ais);
        }
    }
}
