package fi.livi.digitraffic.meri.controller.reader;

import fi.livi.digitraffic.meri.controller.AisMessageConverter;
import fi.livi.digitraffic.meri.controller.ais.AisRadioMsg;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.controller.AisLocker;
import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.controller.VesselMqttSender;
import fi.livi.digitraffic.meri.controller.websocket.WebsocketStatistics;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselMessage;
import fi.livi.digitraffic.meri.service.ais.VesselMetadataService;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.websocketRead.enabled")
public class VesselMetadataRelayListener implements WebsocketListener, AisMessageListener {
    private final VesselMetadataService vesselMetadataService;
    private final VesselMqttSender vesselSender;

    private final AisLocker aisLocker;

    public VesselMetadataRelayListener(final VesselMetadataService vesselMetadataService,
                                       final VesselMqttSender vesselSender,
                                       final AisLocker aisLocker) {
        this.vesselMetadataService = vesselMetadataService;
        this.vesselSender = vesselSender;
        this.aisLocker = aisLocker;
    }

    // Remove
    @Override
    @Transactional
    public void receiveMessage(final String message) {
        final VesselMessage vm = MessageConverter.convertMetadata(message);

        if (vm.validate() && aisLocker.hasLockForAis()) {
            doLogAndSend(vm);
        }
    }

    // Remove
    private void doLogAndSend(final VesselMessage vm) {
        if (isAllowedMmsi(vm.vesselAttributes.mmsi)) {
            final VesselMetadata vessel = new VesselMetadata(vm.vesselAttributes);

            WebsocketStatistics.sentWebsocketStatistics(WebsocketStatistics.WebsocketType.METADATA, 1, 0);
            vesselSender.sendMetadataMessage(vessel);
        } else {
            WebsocketStatistics.sentWebsocketStatistics(WebsocketStatistics.WebsocketType.METADATA, 0, 1);
        }
    }

    // Remove
    private boolean isAllowedMmsi(int mmsi) {
        return vesselMetadataService.findAllowedMmsis().contains(mmsi);
    }

    // Remove
    @Override public void connectionStatus(final ReconnectingHandler.ConnectionStatus status) {
        // no need to do anything
    }

    @Override
    public void receiveMessage(AisRadioMsg message) {
        if (aisLocker.hasLockForAis() && message.isMmsiAllowed()) {
            final VesselMessage vm = AisMessageConverter.convertMetadata(message);

            if (vm.validate()) {
                boolean sendStatus = vesselSender.sendNewAisMessage(new VesselMetadata(vm.vesselAttributes), null);

                VesselLoggingListener.sentAisMessagesStatistics(VesselLoggingListener.AISLoggingType.METADATA, sendStatus);
            }
        }
    }
}