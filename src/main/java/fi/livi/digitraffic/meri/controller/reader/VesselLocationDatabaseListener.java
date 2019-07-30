package fi.livi.digitraffic.meri.controller.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.AisMessageConverter;
import fi.livi.digitraffic.meri.controller.CachedLocker;
import fi.livi.digitraffic.meri.controller.ais.AisRadioMsg;
import fi.livi.digitraffic.meri.model.ais.AISMessage;
import fi.livi.digitraffic.meri.service.ais.VesselLocationService;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.reader.enabled")
public class VesselLocationDatabaseListener implements AisMessageListener {
    private final VesselLocationService vesselLocationService;
    private final CachedLocker aisCachedLocker;

    private final Map<Integer, AISMessage> messageMap = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(VesselLocationDatabaseListener.class);

    public VesselLocationDatabaseListener(final VesselLocationService vesselLocationService,
        final CachedLocker aisCachedLocker) {
        this.vesselLocationService = vesselLocationService;
        this.aisCachedLocker = aisCachedLocker;
    }

    @Scheduled(fixedRate = 1000)
    private void persistQueue() {
        try {
            final List<AISMessage> messages = removeAllMessages();

            if (aisCachedLocker.hasLock()) {
                vesselLocationService.saveLocations(messages);
            }
        } catch(final Exception e) {
            log.error("persist failed", e);
        }
    }

    private synchronized List<AISMessage> removeAllMessages() {
        final List<AISMessage> messages = new ArrayList(messageMap.values());

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
