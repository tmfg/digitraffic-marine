package fi.livi.digitraffic.meri.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.reader.VesselMetadataReader;
import fi.livi.digitraffic.meri.dao.VesselMetadataRepository;
import fi.livi.util.locking.LockingService;
import fi.livi.util.locking.AccessLock;

@Component
public class VesselMetadataReaderController {
    @Autowired
    public VesselMetadataReaderController(@Value("${ais.metadata.5.url}") final String aisLocations5Url,
                                          final LockingService lockingService,
                                          final VesselMetadataRepository vesselMetadataRepository) {
        createVesselMetadataReader(vesselMetadataRepository, lockingService, aisLocations5Url);
    }

    private void createVesselMetadataReader(final VesselMetadataRepository vesselMetadataRepository,
                                            final LockingService lockingService,
                                            final String aisLocations5Url) {
        final AccessLock accessLock = lockingService.lock("AIS-metadata");

        new VesselMetadataReader(aisLocations5Url, vesselMetadataRepository, accessLock);
    }
}
