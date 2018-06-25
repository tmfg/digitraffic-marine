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

import fi.livi.digitraffic.meri.controller.reader.VesselLocationLoggingListener;
import fi.livi.digitraffic.meri.controller.reader.VesselMetadataDatabaseListener;
import fi.livi.digitraffic.meri.controller.reader.VesselMetadataRelayListener;
import fi.livi.digitraffic.meri.controller.reader.WebsocketListener;
import fi.livi.digitraffic.meri.controller.reader.WebsocketLoggingListener;
import fi.livi.digitraffic.meri.controller.reader.WebsocketReader;
import fi.livi.digitraffic.meri.controller.websocket.WebsocketStatistics;
import fi.livi.digitraffic.meri.dao.ais.VesselMetadataRepository;
import fi.livi.digitraffic.meri.service.ais.VesselMetadataService;
import fi.livi.digitraffic.meri.util.service.LockingService;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.websocketRead.enabled")
public class VesselMetadataReaderController {
    private final List<WebsocketReader> readerList;

    @Autowired
    private VesselMetadataReaderController(@Value("${ais.metadata.5.url}") final String aisLocations5Url, final LockingService lockingService,
        final VesselMetadataRepository vesselMetadataRepository, final VesselMetadataService vesselMetadataService, final VesselSender vesselSender,
        final VesselMetadataDatabaseListener vesselMetadataDatabaseListener, final VesselMetadataRelayListener
        vesselMetadataRelayListener) {

        final List<WebsocketListener> listeners = Arrays.asList(
            vesselMetadataDatabaseListener,
            vesselMetadataRelayListener,
            new WebsocketLoggingListener(WebsocketStatistics.WebsocketType.METADATA));

        readerList = Arrays.asList(new WebsocketReader(aisLocations5Url, listeners));

        readerList.forEach(WebsocketReader::initialize);
    }

    @Scheduled(fixedRate = 1000)
    private void idleTimeout() {
        readerList.forEach(WebsocketReader::idleTimeout);
    }

    @PreDestroy
    public void destroy() {
        readerList.forEach(WebsocketReader::destroy);
    }
}
