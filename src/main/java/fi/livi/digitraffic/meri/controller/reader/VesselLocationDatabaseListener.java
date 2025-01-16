package fi.livi.digitraffic.meri.controller.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.livi.digitraffic.common.annotation.NoJobLogging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.common.service.locking.CachedLockingService;
import fi.livi.digitraffic.meri.controller.ais.reader.AisMessageConverter;
import fi.livi.digitraffic.meri.controller.ais.reader.AisMessageListener;
import fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsg;
import fi.livi.digitraffic.meri.dto.ais.external.AISMessage;
import fi.livi.digitraffic.meri.service.ais.VesselLocationService;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.reader.enabled")
public class VesselLocationDatabaseListener implements AisMessageListener {
    private static final Logger log = LoggerFactory.getLogger(VesselLocationDatabaseListener.class);

    private final VesselLocationService vesselLocationService;
    private final CachedLockingService aisCachedLockingService;

    private final Map<Integer, AISMessage> messageMap = new HashMap<>();

    public VesselLocationDatabaseListener(final VesselLocationService vesselLocationService,
                                          final CachedLockingService aisCachedLockingService) {
        this.vesselLocationService = vesselLocationService;
        this.aisCachedLockingService = aisCachedLockingService;
    }

    @NoJobLogging
    @Scheduled(fixedRate = 1000)
    public void persistQueue() {
        try {
            final List<AISMessage> messages = removeAllMessages();

            if (aisCachedLockingService.hasLock()) {
                vesselLocationService.saveLocations(messages);
            }
        } catch(final Exception e) {
            log.error("persist failed", e);
        }
    }

    private synchronized List<AISMessage> removeAllMessages() {
        final List<AISMessage> messages = new ArrayList<>(messageMap.values());

        messageMap.clear();

        return messages;
    }

    @Override
    public synchronized void receiveMessage(final AisRadioMsg message) {
        if (message.isMmsiAllowed()) {
            final AISMessage ais = AisMessageConverter.convertLocation(message);

            if (ais.validate()) {
                messageMap.put(ais.attributes.mmsi, ais);
            }
        }
    }
}
