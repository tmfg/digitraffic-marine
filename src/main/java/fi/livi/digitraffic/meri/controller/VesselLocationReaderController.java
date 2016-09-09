package fi.livi.digitraffic.meri.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import fi.livi.digitraffic.meri.controller.reader.VesselLocationDatabaseListener;
import fi.livi.digitraffic.meri.controller.reader.VesselLocationRelayListener;
import fi.livi.digitraffic.meri.controller.reader.WebsocketListener;
import fi.livi.digitraffic.meri.controller.reader.WebsocketReader;
import fi.livi.digitraffic.meri.dao.VesselLocationRepository;
import fi.livi.util.locking.AccessLock;
import fi.livi.util.locking.LockingService;

@Component
public class VesselLocationReaderController {
    private final List<WebsocketReader> readerList;

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
            final AccessLock accessLock = lockingService.lock("AIS-locations");
            final List<WebsocketListener> listeners = Arrays.asList(
                    new VesselLocationDatabaseListener(vesselLocationRepository, accessLock),
                    new VesselLocationRelayListener(locationSender));

            readerList = Arrays.asList(
                new WebsocketReader(aisLocations9Url, listeners),
                new WebsocketReader(aisLocations27Url, listeners),
                new WebsocketReader(aisLocations123Url, listeners)
            );

            readerList.stream().forEach(x -> x.initialize());
        } else {
            readerList = Collections.emptyList();
        }
    }

    @PreDestroy
    public void destroy() {
        readerList.stream().forEach(x -> x.destroy());
    }
}
