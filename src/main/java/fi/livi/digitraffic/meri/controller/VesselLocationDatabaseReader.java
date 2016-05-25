package fi.livi.digitraffic.meri.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.dao.VesselLocationRepository;
import fi.livi.digitraffic.meri.domain.VesselLocation;
import fi.livi.digitraffic.meri.model.AISMessage;

@Component
public class VesselLocationDatabaseReader extends VesselLocationReader {
    private VesselLocationRepository vesselLocationRepository;

    @Autowired
    public VesselLocationDatabaseReader(@Value("${ais.locations.url}") final String serverAddress,
                                        final VesselLocationRepository vesselLocationRepository) {
        super(serverAddress);
        this.vesselLocationRepository = vesselLocationRepository;
    }

    @Override
    protected void handleMessage(final AISMessage message) {
        vesselLocationRepository.save(new VesselLocation(message));
    }
}
