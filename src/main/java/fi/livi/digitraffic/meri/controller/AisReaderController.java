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

import fi.livi.digitraffic.meri.service.ais.VesselMetadataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.reader.enabled")
public class AisReaderController implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(AisReaderController.class);

    private final List<AisMessageListener> aisLocationListeners;
    private final List<AisMessageListener> aisMetadataListeners;

    private final AtomicBoolean running = new AtomicBoolean(false);

    private final AisMessageReader reader;
    private final VesselMetadataService vesselMetadataService;
    private final VesselLoggingListener vesselLoggingListener;

    @Autowired
    public AisReaderController(final AisMessageReader reader,
                               final VesselMetadataService vesselMetadataService,
                               final VesselMetadataDatabaseListener vesselMetadataDatabaseListener,
                               final VesselMetadataRelayListener vesselMetadataRelayListener,
                               final VesselLocationDatabaseListener vesselLocationDatabaseListener,
                               final VesselLocationRelayListener vesselLocationRelayListener,
                               final VesselLoggingListener vesselLoggingListener) {

        this.reader = reader;
        this.vesselLoggingListener = vesselLoggingListener;
        this.vesselMetadataService = vesselMetadataService;

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
                    updateMmsiAllowedStatus(msg);

                    if (msg.getMessageType() == AisRadioMsg.MessageType.POSITION) {
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

    private void updateMmsiAllowedStatus(AisRadioMsg msg) {
        if (msg.getMessageType() == AisRadioMsg.MessageType.POSITION) {
            msg.setMmsiAllowed(vesselMetadataService.findAllowedMmsis().contains(msg.getUserId()));
        } else {
            // Check if class B message
            if (msg.getMessageClass() == AisRadioMsg.MessageClass.CLASS_B) {
                log.info("Received class-B ais radio message");

                // NOTE! For default all class B messages are ignored unless mmsi is added to allowed list
                msg.setMmsiAllowed(vesselMetadataService.findAllowedMmsis().contains(msg.getUserId()));
            } else {
                // Only fishing vessels are excluded
                msg.setMmsiAllowed(!VesselMetadataService.FORBIDDEN_SHIP_TYPES.contains(AisMessageConverter.getShipType(msg)));
            }
        }
    }
}
