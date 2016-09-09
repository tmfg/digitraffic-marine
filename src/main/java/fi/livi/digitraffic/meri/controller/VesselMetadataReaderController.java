package fi.livi.digitraffic.meri.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import fi.livi.digitraffic.meri.controller.reader.VesselMetadataDatabaseListener;
import fi.livi.digitraffic.meri.controller.reader.WebsocketListener;
import fi.livi.digitraffic.meri.controller.reader.WebsocketReader;
import fi.livi.digitraffic.meri.dao.VesselMetadataRepository;
import fi.livi.util.locking.AccessLock;
import fi.livi.util.locking.LockingService;

@Component
public class VesselMetadataReaderController {
    private final List<WebsocketReader> readerList;

    @Autowired
    private VesselMetadataReaderController(
                                          @Value("${config.test}") final String configTest,
                                          @Value("${ais.metadata.5.url}") final String aisLocations5Url,
                                          final LockingService lockingService,
                                          final VesselMetadataRepository vesselMetadataRepository) {

        if(StringUtils.isEmpty(configTest)) {
            final AccessLock accessLock = lockingService.lock("AIS-metadata");
            final List<WebsocketListener> listeners = Arrays.asList(new VesselMetadataDatabaseListener(vesselMetadataRepository, accessLock));

            readerList = Arrays.asList(new WebsocketReader(aisLocations5Url, listeners));

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
