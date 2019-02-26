package fi.livi.digitraffic.meri.controller.reader;

import fi.livi.digitraffic.meri.controller.AisMessageConverter;
import fi.livi.digitraffic.meri.controller.ais.AisRadioMsg;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.AisLocker;
import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.controller.VesselMqttSender;
import fi.livi.digitraffic.meri.controller.websocket.WebsocketStatistics;
import fi.livi.digitraffic.meri.model.ais.AISMessage;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeature;
import fi.livi.digitraffic.meri.service.ais.VesselLocationConverter;
import fi.livi.digitraffic.meri.service.ais.VesselMetadataService;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.websocketRead.enabled")
public class VesselLocationRelayListener implements WebsocketListener, AisMessageListener {
    private final VesselMqttSender vesselSender;
    private final VesselMetadataService vesselMetadataService;

    private final AisLocker aisLocker;

    public VesselLocationRelayListener(final VesselMqttSender vesselSender,
                                       final VesselMetadataService vesselMetadataService,
                                       final AisLocker aisLocker) {
        this.vesselSender = vesselSender;
        this.vesselMetadataService = vesselMetadataService;
        this.aisLocker = aisLocker;
    }

    // Remove
    @Override
    public void receiveMessage(final String message) {
        final AISMessage ais = MessageConverter.convertLocation(message);

        if (ais.validate() && aisLocker.hasLockForAis()) {
            doLogAndSend(ais);
        }
    }

    // Remove
    private void doLogAndSend(final AISMessage ais) {
        if (isAllowedMmsi(ais.attributes.mmsi)) {
            final VesselLocationFeature feature = VesselLocationConverter.convert(ais);

            WebsocketStatistics.sentWebsocketStatistics(WebsocketStatistics.WebsocketType.LOCATIONS, 1, 0);
            vesselSender.sendLocationMessage(feature);
        } else {
            WebsocketStatistics.sentWebsocketStatistics(WebsocketStatistics.WebsocketType.LOCATIONS, 0, 1);
        }
    }

    // Remove
    private boolean isAllowedMmsi(final int mmsi) {
        return vesselMetadataService.findAllowedMmsis().contains(mmsi);
    }

    // Remove
    @Override
    public void connectionStatus(final ReconnectingHandler.ConnectionStatus status) {
        // no need to do anything
    }

    @Override
    public void receiveMessage(AisRadioMsg message) {
        if (aisLocker.hasLockForAis()) {
            final AISMessage ais = AisMessageConverter.convertLocation(message);

            if (ais.validate() && message.isMmsiAllowed()) {
                // TODO! change name sendMessage
                boolean sendStatus = vesselSender.sendNewAisMessage(null, VesselLocationConverter.convert(ais));

                VesselLoggingListener.sentAisMessagesStatistics(VesselLoggingListener.AISLoggingType.POSITION, sendStatus);
            }
        }
    }
}
