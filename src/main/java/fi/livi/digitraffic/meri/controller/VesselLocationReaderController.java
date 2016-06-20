package fi.livi.digitraffic.meri.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.reader.VesselLocationDatabaseReader;
import fi.livi.digitraffic.meri.controller.reader.VesselLocationRelayReader;
import fi.livi.digitraffic.meri.dao.VesselLocationRepository;
import fi.livi.util.locking.LockingService;
import fi.livi.util.locking.AccessLock;

@Component
public class VesselLocationReaderController {
    @Autowired
    public VesselLocationReaderController(
            @Value("${ais.locations.123.url}") final String aisLocations123Url,
            @Value("${ais.locations.27.url}") final String aisLocations27Url,
            @Value("${ais.locations.9.url}") final String aisLocations9Url,
                                          final VesselLocationRepository vesselLocationRepository,
                                          final LockingService lockingService,
                                          final LocationSender locationSender) {
        createVesselLocationRelayReaders(locationSender, aisLocations123Url, aisLocations27Url, aisLocations9Url);
        createVesselLocationDatabaseReaders(lockingService, vesselLocationRepository, aisLocations123Url, aisLocations27Url, aisLocations9Url);
    }

    private void createVesselLocationRelayReaders(final LocationSender locationSender, final String aisLocations123Url, final String aisLocations27Url,
                                                  final String aisLocations9Url) {
        new VesselLocationRelayReader(aisLocations123Url, locationSender);
        new VesselLocationRelayReader(aisLocations27Url, locationSender);
        new VesselLocationRelayReader(aisLocations9Url, locationSender);
    }

    private void createVesselLocationDatabaseReaders(final LockingService lockingService, final VesselLocationRepository vesselLocationRepository,
                                                     final String aisLocations123Url, final String aisLocations27Url, final String aisLocations9Url) {
        final AccessLock accessLock = lockingService.lock("AIS-locations");

        new VesselLocationDatabaseReader(aisLocations123Url, vesselLocationRepository, accessLock);
        new VesselLocationDatabaseReader(aisLocations27Url, vesselLocationRepository, accessLock);
        new VesselLocationDatabaseReader(aisLocations9Url, vesselLocationRepository, accessLock);
    }
}
