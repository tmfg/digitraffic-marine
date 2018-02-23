package fi.livi.digitraffic.meri.controller;

import java.util.Arrays;
import java.util.List;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.reader.VesselLocationDatabaseListener;
import fi.livi.digitraffic.meri.controller.reader.VesselLocationRelayListener;
import fi.livi.digitraffic.meri.controller.reader.WebsocketListener;
import fi.livi.digitraffic.meri.controller.reader.WebsocketLoggingListener;
import fi.livi.digitraffic.meri.controller.reader.WebsocketReader;
import fi.livi.digitraffic.meri.controller.websocket.WebsocketStatistics;
import fi.livi.digitraffic.meri.dao.ais.VesselLocationRepository;
import fi.livi.digitraffic.meri.service.ais.VesselMetadataService;
import fi.livi.digitraffic.meri.util.service.LockingService;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.websocketRead.enabled")
public class VesselLocationReaderController {
    private final List<WebsocketReader> readerList;

    @Autowired
    private VesselLocationReaderController(
                @Value("${ais.locations.123.url}") final String aisLocations123Url,
                @Value("${ais.locations.27.url}") final String aisLocations27Url,
                @Value("${ais.locations.9.url}") final String aisLocations9Url,
                final VesselLocationRepository vesselLocationRepository, final LockingService lockingService,
                final VesselMetadataService vesselMetadataService, final VesselSender vesselSender) {
            final List<WebsocketListener> listeners = Arrays.asList(
                    new VesselLocationDatabaseListener(vesselLocationRepository, lockingService),
                    new VesselLocationRelayListener(vesselSender, vesselMetadataService),
                    new WebsocketLoggingListener(WebsocketStatistics.WebsocketType.LOCATIONS)
            );

            readerList = Arrays.asList(
                new WebsocketReader(aisLocations9Url, listeners),
                new WebsocketReader(aisLocations27Url, listeners),
                new WebsocketReader(aisLocations123Url, listeners)
            );

            readerList.forEach(WebsocketReader::initialize);
    }

    @Scheduled(fixedRate = 10000)
    private void idleTimeout() {
        readerList.forEach(WebsocketReader::idleTimeout);
    }

    @PreDestroy
    public void destroy() {
        readerList.forEach(WebsocketReader::destroy);
    }
}
