package fi.livi.digitraffic.meri.controller.reader;

import fi.livi.digitraffic.meri.dao.VesselLocationRepository;
import fi.livi.digitraffic.meri.domain.VesselLocation;
import fi.livi.digitraffic.meri.model.AISMessage;
import fi.livi.util.locking.AccessLock;

public class VesselLocationDatabaseReader extends VesselLocationReader {
    private final VesselLocationRepository vesselLocationRepository;
    private final AccessLock accessLock;

    public VesselLocationDatabaseReader(final String aisLocationsUrl,
                                        final VesselLocationRepository vesselLocationRepository,
                                        final AccessLock accessLock) {
        super(aisLocationsUrl);
        this.vesselLocationRepository = vesselLocationRepository;
        this.accessLock = accessLock;
    }

    @Override
    protected void handleMessage(final AISMessage message) {
        if(accessLock.get()) {
            vesselLocationRepository.save(new VesselLocation(message));
        }
    }
}
