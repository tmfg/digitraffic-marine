package fi.livi.digitraffic.meri.controller.reader;

import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.model.AISMessage;

public abstract class VesselLocationReader extends WebsocketReader<AISMessage> {
    public VesselLocationReader(final String aisLocationsUrl) {
        super(aisLocationsUrl);
    }

    @Override
    protected AISMessage convert(final String message) {
        return MessageConverter.convertLocation(message);
    }
}
