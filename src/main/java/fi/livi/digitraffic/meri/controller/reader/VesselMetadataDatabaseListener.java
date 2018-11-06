package fi.livi.digitraffic.meri.controller.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.AisLocker;
import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.dao.ais.VesselMetadataRepository;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselMessage;
import fi.livi.digitraffic.meri.util.service.LockingService;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.websocketRead.enabled")
public class VesselMetadataDatabaseListener implements WebsocketListener {
    private final VesselMetadataRepository vesselMetadataRepository;
    private final AisLocker aisLocker;

    private final Map<Integer, VesselMessage> messageMap = new HashMap<>();

    public VesselMetadataDatabaseListener(final VesselMetadataRepository vesselMetadataRepository, final LockingService lockingService,
        final AisLocker aisLocker) {
        this.vesselMetadataRepository = vesselMetadataRepository;
        this.aisLocker = aisLocker;
    }

    @Override
    public synchronized void receiveMessage(final String message) {
        final VesselMessage vm = MessageConverter.convertMetadata(message);

        if (vm.validate()) {
            messageMap.put(vm.vesselAttributes.mmsi, vm);
        }
    }

    @Scheduled(fixedRate = 1000)
    private void persistQueue() {
        final List<VesselMessage> messages = removeAllMessages();

        if(aisLocker.hasLockForAis()) {
            final List<VesselMetadata> vessels = messages.stream()
                .map(v -> new VesselMetadata(v.vesselAttributes))
                .collect(Collectors.toList());

            vesselMetadataRepository.saveAll(vessels);
        }
    }

    private synchronized List<VesselMessage> removeAllMessages() {
        final List<VesselMessage> messages = new ArrayList(messageMap.values());

        messageMap.clear();

        return messages;
    }

    @Override public void connectionStatus(final ReconnectingHandler.ConnectionStatus status) {
        // no need to do anything
    }
}
