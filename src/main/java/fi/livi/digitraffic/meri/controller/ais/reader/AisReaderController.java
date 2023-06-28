package fi.livi.digitraffic.meri.controller.ais.reader;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import fi.livi.digitraffic.meri.controller.reader.*;

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

    @Autowired
    public AisReaderController(final AisMessageReader reader,
                               final VesselMetadataService vesselMetadataService,
                               final VesselMetadataDatabaseListener vesselMetadataDatabaseListener,
                               final VesselMetadataRelayListenerV2 vesselMetadataRelayListenerV2,
                               final VesselLocationDatabaseListener vesselLocationDatabaseListener,
                               final VesselLocationRelayListenerV2 vesselLocationRelayListenerV2,
                               final VesselLoggingListener vesselLoggingListener) {

        this.reader = reader;
        this.vesselMetadataService = vesselMetadataService;

        aisLocationListeners = Arrays.asList(
            vesselLocationDatabaseListener,
            vesselLocationRelayListenerV2,
            vesselLoggingListener);

        aisMetadataListeners = Arrays.asList(
            vesselMetadataDatabaseListener,
            vesselMetadataRelayListenerV2,
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
                final AisRadioMsg msg = reader.getAisRadioMessage();

                if (msg != null) {
                    updateMmsiAllowedStatus(msg);

                    if (msg.getMessageType() == AisRadioMsg.MessageType.POSITION) {
                        aisLocationListeners.forEach(aisMessageListener -> aisMessageListener.receiveMessage(msg));
                    } else {
                        aisMetadataListeners.forEach(aisMessageListener -> aisMessageListener.receiveMessage(msg));
                    }
                }
            } catch (final Exception e) {
                log.error("Failed to prosess AIS-message", e);
            }
        }
    }

    private void updateMmsiAllowedStatus(final AisRadioMsg msg) {
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
