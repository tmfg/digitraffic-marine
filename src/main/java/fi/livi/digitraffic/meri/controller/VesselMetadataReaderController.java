package fi.livi.digitraffic.meri.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.reader.VesselMetadataDatabaseListener;
import fi.livi.digitraffic.meri.controller.reader.WebsocketListener;
import fi.livi.digitraffic.meri.controller.reader.WebsocketLoggingListener;
import fi.livi.digitraffic.meri.controller.reader.WebsocketReader;
import fi.livi.digitraffic.meri.controller.websocket.WebsocketStatistics;
import fi.livi.digitraffic.meri.dao.ais.VesselMetadataRepository;
import fi.livi.util.locking.AccessLock;
import fi.livi.util.locking.LockingService;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
public class VesselMetadataReaderController {
    private final List<WebsocketReader> readerList;

    @Autowired
    private VesselMetadataReaderController(
            @Value("${ais.metadata.5.url}") final String aisLocations5Url,
            final LockingService lockingService,
            final VesselMetadataRepository vesselMetadataRepository) {
        final AccessLock accessLock = lockingService.lock("AIS-metadata");
        final List<WebsocketListener> listeners = Arrays.asList(
                new VesselMetadataDatabaseListener(vesselMetadataRepository, accessLock),
                new WebsocketLoggingListener(WebsocketStatistics.WebsocketType.METADATA)
        );

        readerList = Arrays.asList(new WebsocketReader(aisLocations5Url, listeners));

        readerList.stream().forEach(WebsocketReader::initialize);
    }

    @PreDestroy
    public void destroy() {
        readerList.stream().forEach(WebsocketReader::destroy);
    }
}
