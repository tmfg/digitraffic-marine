package fi.livi.digitraffic.meri.controller.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.livi.digitraffic.meri.controller.AisMessageConverter;
import fi.livi.digitraffic.meri.controller.ais.AisRadioMsg;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.AisLocker;
import fi.livi.digitraffic.meri.model.ais.AISMessage;
import fi.livi.digitraffic.meri.service.ais.VesselLocationService;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.reader.enabled")
public class VesselLocationDatabaseListener implements AisMessageListener {
    private final VesselLocationService vesselLocationService;
    private final AisLocker aisLocker;

    private final Map<Integer, AISMessage> messageMap = new HashMap<>();

    public VesselLocationDatabaseListener(final VesselLocationService vesselLocationService, final AisLocker aisLocker) {
        this.vesselLocationService = vesselLocationService;
        this.aisLocker = aisLocker;
    }

    @Scheduled(fixedRate = 1000)
    private void persistQueue() {
        final List<AISMessage> messages = removeAllMessages();

        if (aisLocker.hasLockForAis()) {
            vesselLocationService.saveLocations(messages);
        }
    }

    private synchronized List<AISMessage> removeAllMessages() {
        final List<AISMessage> messages = new ArrayList(messageMap.values());

        messageMap.clear();

        return messages;
    }

    @Override
    public void receiveMessage(final AisRadioMsg message) {
        if (message.isMmsiAllowed()) {
            final AISMessage ais = AisMessageConverter.convertLocation(message);

            if (ais.validate()) {
                messageMap.put(ais.attributes.mmsi, ais);
            }
        }
    }
}
