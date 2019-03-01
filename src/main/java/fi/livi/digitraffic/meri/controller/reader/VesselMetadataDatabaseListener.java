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
import fi.livi.digitraffic.meri.model.ais.VesselMessage;
import fi.livi.digitraffic.meri.service.ais.VesselMetadataService;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.reader.enabled")
public class VesselMetadataDatabaseListener implements AisMessageListener {
    private final VesselMetadataService vesselMetadataService;
    private final AisLocker aisLocker;

    private final Map<Integer, VesselMessage> messageMap = new HashMap<>();

    public VesselMetadataDatabaseListener(final VesselMetadataService vesselMetadataService, final AisLocker aisLocker) {
        this.vesselMetadataService = vesselMetadataService;
        this.aisLocker = aisLocker;
    }

    @Scheduled(fixedRate = 1000)
    private void persistQueue() {
        final List<VesselMessage> messages = removeAllMessages();

        if (aisLocker.hasLockForAis()) {
            vesselMetadataService.saveVessels(messages);
        }
    }

    private synchronized List<VesselMessage> removeAllMessages() {
        final List<VesselMessage> messages = new ArrayList(messageMap.values());

        messageMap.clear();

        return messages;
    }


    @Override
    public void receiveMessage(final AisRadioMsg message) {
        final VesselMessage vm = AisMessageConverter.convertMetadata(message);

        if (vm.validate()) {
            messageMap.put(vm.vesselAttributes.mmsi, vm);
        }
    }
}
