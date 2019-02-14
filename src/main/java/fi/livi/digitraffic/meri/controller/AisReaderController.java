package fi.livi.digitraffic.meri.controller;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import fi.livi.digitraffic.meri.controller.ais.AisRadioMsg;
import fi.livi.digitraffic.meri.controller.reader.AisMessageListener;
import fi.livi.digitraffic.meri.controller.reader.AisMessageReader;
import fi.livi.digitraffic.meri.controller.reader.VesselLocationDatabaseListener;
import fi.livi.digitraffic.meri.controller.reader.VesselLocationRelayListener;
import fi.livi.digitraffic.meri.controller.reader.VesselLoggingListener;
import fi.livi.digitraffic.meri.controller.reader.VesselMetadataDatabaseListener;
import fi.livi.digitraffic.meri.controller.reader.VesselMetadataRelayListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty("ais.reader.enabled")
public class AisReaderController implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(AisReaderController.class);

    private final List<AisMessageListener> aisLocationListeners;
    private final List<AisMessageListener> aisMetadataListeners;

    private final AtomicBoolean running = new AtomicBoolean(false);

    private final AisMessageReader reader;
    private final VesselLoggingListener vesselLoggingListener;

    public AisReaderController(final AisMessageReader reader,
                               final VesselMetadataDatabaseListener vesselMetadataDatabaseListener,
                               final VesselMetadataRelayListener vesselMetadataRelayListener,
                               final VesselLocationDatabaseListener vesselLocationDatabaseListener,
                               final VesselLocationRelayListener vesselLocationRelayListener,
                               final VesselLoggingListener vesselLoggingListener) {

        this.reader = reader;
        this.vesselLoggingListener = vesselLoggingListener;

        aisLocationListeners = Arrays.asList(
            vesselLocationDatabaseListener,
            vesselLocationRelayListener,
            vesselLoggingListener);

        aisMetadataListeners = Arrays.asList(
            vesselMetadataDatabaseListener,
            vesselMetadataRelayListener,
            vesselLoggingListener);
    }

    @PostConstruct
    public void setUp() {
        running.set(true);

        Executors.newSingleThreadExecutor().submit(this);
    }

    @PreDestroy
    public void destroy() {
        running.set(false);

        // TODO! generate&send kill message
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                AisRadioMsg msg = reader.getAisRadioMessage();

                if (msg != null) {
                    if (msg.isPositionMsg()) {
                        aisLocationListeners.forEach(aisMessageListener -> aisMessageListener.receiveMessage(msg));
                    } else {
                        aisMetadataListeners.forEach(aisMessageListener -> aisMessageListener.receiveMessage(msg));
                    }
                }
            } catch (Exception e) {
                log.error("Failed to prosess AIS-message", e);
            }
        }
    }
}
