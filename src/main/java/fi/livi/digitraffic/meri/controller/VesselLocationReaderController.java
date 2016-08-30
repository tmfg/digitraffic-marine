package fi.livi.digitraffic.meri.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import fi.livi.digitraffic.meri.controller.reader.VesselLocationDatabaseReader;
import fi.livi.digitraffic.meri.controller.reader.VesselLocationRelayReader;
import fi.livi.digitraffic.meri.controller.reader.WebsocketReader;
import fi.livi.digitraffic.meri.dao.VesselLocationRepository;
import fi.livi.util.locking.AccessLock;
import fi.livi.util.locking.LockingService;

@Component
public class VesselLocationReaderController {
    private final List<WebsocketReader> readerList = new ArrayList<>();

    @Autowired
    private VesselLocationReaderController(
            @Value("${config.test}") final String configTest,
            @Value("${ais.locations.123.url}") final String aisLocations123Url,
            @Value("${ais.locations.27.url}") final String aisLocations27Url,
            @Value("${ais.locations.9.url}") final String aisLocations9Url,
            final VesselLocationRepository vesselLocationRepository,
            final LockingService lockingService,
            final LocationSender locationSender) {
        if(StringUtils.isEmpty(configTest)) {
            createVesselLocationRelayReaders(locationSender, aisLocations123Url, aisLocations27Url, aisLocations9Url);
            createVesselLocationDatabaseReaders(lockingService, vesselLocationRepository, aisLocations123Url, aisLocations27Url, aisLocations9Url);
        }

        readerList.stream().forEach(x -> x.initialize());
    }

    private void createVesselLocationRelayReaders(final LocationSender locationSender,
                                                  final String aisLocations123Url,
                                                  final String aisLocations27Url,
                                                  final String aisLocations9Url) {
        readerList.add(new VesselLocationRelayReader(aisLocations123Url, locationSender));
        readerList.add(new VesselLocationRelayReader(aisLocations27Url, locationSender));
        readerList.add(new VesselLocationRelayReader(aisLocations9Url, locationSender));
    }

    private void createVesselLocationDatabaseReaders(final LockingService lockingService, final VesselLocationRepository vesselLocationRepository,
                                                     final String aisLocations123Url, final String aisLocations27Url, final String aisLocations9Url) {
        final AccessLock accessLock = lockingService.lock("AIS-locations");

        readerList.add(new VesselLocationDatabaseReader(aisLocations123Url, vesselLocationRepository, accessLock));
        readerList.add(new VesselLocationDatabaseReader(aisLocations27Url, vesselLocationRepository, accessLock));
        readerList.add(new VesselLocationDatabaseReader(aisLocations9Url, vesselLocationRepository, accessLock));
    }

    @PreDestroy
    public void destroy() {
        readerList.stream().forEach(x -> x.destroy());
    }
}
