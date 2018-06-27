package fi.livi.digitraffic.meri.controller.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.dao.ais.VesselLocationRepository;
import fi.livi.digitraffic.meri.domain.ais.VesselLocation;
import fi.livi.digitraffic.meri.model.ais.AISMessage;
import fi.livi.digitraffic.meri.util.service.LockingService;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.websocketRead.enabled")
public class VesselLocationDatabaseListener implements WebsocketListener {
    private final VesselLocationRepository vesselLocationRepository;
    private final LockingService lockingService;

    private final Map<Integer, AISMessage> messageMap = new HashMap<>();

    public VesselLocationDatabaseListener(final VesselLocationRepository vesselLocationRepository,
                                          final LockingService lockingService) {
        this.vesselLocationRepository = vesselLocationRepository;
        this.lockingService = lockingService;
    }

    @Override
    public synchronized void receiveMessage(final String message) {
        final AISMessage ais = MessageConverter.convertLocation(message);

        if(ais.validate()) {
            // remove duplicates, if any
            messageMap.put(ais.attributes.mmsi, ais);
        }
    }

    @Scheduled(fixedRate = 1000)
    private void persistQueue() {
        final List<AISMessage> messages = removeAllMessages();

        if(getLock()) {
            final List<VesselLocation> locations = messages.stream().map(VesselLocation::new).collect(Collectors.toList());

            vesselLocationRepository.saveAll(locations);
        }
    }

    private synchronized List<AISMessage> removeAllMessages() {
        final List<AISMessage> messages = new ArrayList(messageMap.values());

        messageMap.clear();

        return messages;
    }

    private boolean getLock() {
        return lockingService.acquireLockForAis();
    }

    @Override
    public void connectionStatus(final ReconnectingHandler.ConnectionStatus status) {
        // no need to do anything
    }
}
