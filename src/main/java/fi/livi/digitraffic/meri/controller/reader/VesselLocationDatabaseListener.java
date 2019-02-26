package fi.livi.digitraffic.meri.controller.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fi.livi.digitraffic.meri.controller.AisMessageConverter;
import fi.livi.digitraffic.meri.controller.ais.AisRadioMsg;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.AisLocker;
import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.dao.ais.VesselLocationRepository;
import fi.livi.digitraffic.meri.domain.ais.VesselLocation;
import fi.livi.digitraffic.meri.model.ais.AISMessage;
import fi.livi.digitraffic.meri.service.ais.VesselLocationService;
import fi.livi.digitraffic.meri.util.service.LockingService;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.websocketRead.enabled")
public class VesselLocationDatabaseListener implements WebsocketListener, AisMessageListener {
    private final VesselLocationService vesselLocationService;
    private final AisLocker aisLocker;

    private final Map<Integer, AISMessage> messageMap = new HashMap<>();

    public VesselLocationDatabaseListener(final VesselLocationService vesselLocationService,
                                          final AisLocker aisLocker) {
        this.vesselLocationService = vesselLocationService;
        this.aisLocker = aisLocker;
    }

    // Remove
    @Override
    public synchronized void receiveMessage(final String message) {
        final AISMessage ais = MessageConverter.convertLocation(message);

        if (ais.validate()) {
            // remove duplicates, if any
            messageMap.put(ais.attributes.mmsi, ais);
        }
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

    // Remove
    @Override
    public void connectionStatus(final ReconnectingHandler.ConnectionStatus status) {
        // no need to do anything
    }

    @Override
    public void receiveMessage(AisRadioMsg message) {
        final AISMessage ais = AisMessageConverter.convertLocation(message);

        if (ais.validate() && message.isMmsiAllowed()) {
            messageMap.put(ais.attributes.mmsi, ais);
        }
    }
}
