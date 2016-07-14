package fi.livi.digitraffic.meri.controller.reader;

import fi.livi.digitraffic.meri.controller.LocationSender;
import fi.livi.digitraffic.meri.model.AISMessage;

public class VesselLocationRelayReader extends VesselLocationReader {
    private final LocationSender locationSender;

    public VesselLocationRelayReader(final String aisLocationsUrl, final LocationSender locationSender) {
        super(aisLocationsUrl);

        this.locationSender = locationSender;
    }

    @Override
    protected void handleMessage(final AISMessage message) {
        locationSender.sendMessage(message);
    }
}
