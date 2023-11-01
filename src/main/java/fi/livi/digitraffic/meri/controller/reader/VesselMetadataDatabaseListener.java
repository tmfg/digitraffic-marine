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

import fi.livi.digitraffic.meri.controller.ais.reader.AisMessageConverter;
import fi.livi.digitraffic.meri.controller.ais.reader.AisMessageListener;
import fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsg;
import fi.livi.digitraffic.meri.dto.ais.external.VesselMessage;
import fi.livi.digitraffic.meri.service.CachedLockerService;
import fi.livi.digitraffic.meri.service.ais.VesselMetadataService;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.reader.enabled")
public class VesselMetadataDatabaseListener implements AisMessageListener {
    private final VesselMetadataService vesselMetadataService;
    private final CachedLockerService aisCachedLocker;

    private final Map<Integer, VesselMessage> messageMap = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(VesselMetadataDatabaseListener.class);

    public VesselMetadataDatabaseListener(final VesselMetadataService vesselMetadataService,
        final CachedLockerService aisCachedLocker) {
        this.vesselMetadataService = vesselMetadataService;
        this.aisCachedLocker = aisCachedLocker;
    }

    @Scheduled(fixedRate = 1000)
    public void persistQueue() {
        try {
            final List<VesselMessage> messages = removeAllMessages();

            if (aisCachedLocker.hasLock()) {
                vesselMetadataService.saveVessels(messages);
            }
        } catch(final Exception e) {
            log.error("persist failed", e);
        }
    }

    private synchronized List<VesselMessage> removeAllMessages() {
        final List<VesselMessage> messages = new ArrayList(messageMap.values());

        messageMap.clear();

        return messages;
    }


    @Override
    public synchronized void receiveMessage(final AisRadioMsg message) {
        final VesselMessage vm = AisMessageConverter.convertMetadata(message);

        if (vm.validate()) {
            messageMap.put(vm.vesselAttributes.mmsi, vm);
        }
    }
}
