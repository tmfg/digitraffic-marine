package fi.livi.digitraffic.meri.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.model.AISMessage;

@Component
public class VesselLocationRelayReader extends VesselLocationReader {
    private final LocationSender locationSender;

    @Autowired
    public VesselLocationRelayReader(@Value("${ais.locations.url}") final String aisLocationsUrl, final LocationSender locationSender) {
        super(aisLocationsUrl);

        this.locationSender = locationSender;
    }

    @Override
    protected void handleMessage(final AISMessage message) {
        locationSender.sendMessage(message);
    }
}
