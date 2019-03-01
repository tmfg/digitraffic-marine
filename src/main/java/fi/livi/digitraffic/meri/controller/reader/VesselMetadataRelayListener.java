package fi.livi.digitraffic.meri.controller.reader;

import fi.livi.digitraffic.meri.controller.AisMessageConverter;
import fi.livi.digitraffic.meri.controller.ais.AisRadioMsg;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.AisLocker;
import fi.livi.digitraffic.meri.controller.VesselMqttSender;
import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;
import fi.livi.digitraffic.meri.model.ais.VesselMessage;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.reader.enabled")
public class VesselMetadataRelayListener implements AisMessageListener {
    private final VesselMqttSender vesselSender;
    private final AisLocker aisLocker;

    public VesselMetadataRelayListener(final VesselMqttSender vesselSender, final AisLocker aisLocker) {
        this.vesselSender = vesselSender;
        this.aisLocker = aisLocker;
    }

    @Override
    public void receiveMessage(final AisRadioMsg message) {
        if (message.isMmsiAllowed() && aisLocker.hasLockForAis()) {
            final VesselMessage vm = AisMessageConverter.convertMetadata(message);

            if (vm.validate()) {
                final boolean sendStatus = vesselSender.sendMetadataMessage(new VesselMetadata(vm.vesselAttributes));

                VesselLoggingListener.sentAisMessagesStatistics(VesselLoggingListener.AISLoggingType.METADATA, sendStatus);
            }
        }
    }
}