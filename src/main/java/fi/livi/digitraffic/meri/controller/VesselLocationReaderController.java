package fi.livi.digitraffic.meri.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.reader.VesselLocationDatabaseListener;
import fi.livi.digitraffic.meri.controller.reader.VesselLocationRelayListener;
import fi.livi.digitraffic.meri.controller.reader.WebsocketListener;
import fi.livi.digitraffic.meri.controller.reader.WebsocketLoggingListener;
import fi.livi.digitraffic.meri.controller.reader.WebsocketReader;
import fi.livi.digitraffic.meri.controller.websocket.WebsocketStatistics;
import fi.livi.digitraffic.meri.dao.ais.VesselLocationRepository;
import fi.livi.digitraffic.meri.service.ais.VesselMetadataService;
import fi.livi.digitraffic.util.service.LockingService;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
public class VesselLocationReaderController {
    private final List<WebsocketReader> readerList;

    @Autowired
    private VesselLocationReaderController(
            @Value("${ais.locations.123.url}") final String aisLocations123Url,
            @Value("${ais.locations.27.url}") final String aisLocations27Url,
            @Value("${ais.locations.9.url}") final String aisLocations9Url,
            final VesselLocationRepository vesselLocationRepository,
            final LockingService lockingService,
            final VesselMetadataService vesselMetadataService,
            final VesselsSender vesselsSender) {
            final List<WebsocketListener> listeners = Arrays.asList(
                    new VesselLocationDatabaseListener(vesselLocationRepository, lockingService),
                    new VesselLocationRelayListener(vesselsSender, vesselMetadataService),
                    new WebsocketLoggingListener(WebsocketStatistics.WebsocketType.LOCATIONS)
            );

            readerList = Arrays.asList(
                new WebsocketReader(aisLocations9Url, listeners),
                new WebsocketReader(aisLocations27Url, listeners),
                new WebsocketReader(aisLocations123Url, listeners)
            );

            readerList.stream().forEach(WebsocketReader::initialize);
    }

    @PreDestroy
    public void destroy() {
        readerList.stream().forEach(WebsocketReader::destroy);
    }
}
