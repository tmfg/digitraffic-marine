package fi.livi.digitraffic.meri.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import fi.livi.digitraffic.meri.controller.reader.VesselMetadataReader;
import fi.livi.digitraffic.meri.dao.VesselMetadataRepository;
import fi.livi.util.locking.AccessLock;
import fi.livi.util.locking.LockingService;

@Component
public class VesselMetadataReaderController {
    @Autowired
    private VesselMetadataReaderController(
                                          @Value("${config.test}") final String configTest,
                                          @Value("${ais.metadata.5.url}") final String aisLocations5Url,
                                          final LockingService lockingService,
                                          final VesselMetadataRepository vesselMetadataRepository) {

        if(StringUtils.isEmpty(configTest)) {
            createVesselMetadataReader(vesselMetadataRepository, lockingService, aisLocations5Url);
        }
    }

    private static void createVesselMetadataReader(final VesselMetadataRepository vesselMetadataRepository,
                                            final LockingService lockingService,
                                            final String aisLocations5Url) {
        final AccessLock accessLock = lockingService.lock("AIS-metadata");

        new VesselMetadataReader(aisLocations5Url, vesselMetadataRepository, accessLock).initialize();
    }
}
