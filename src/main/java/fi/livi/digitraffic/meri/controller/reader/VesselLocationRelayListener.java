package fi.livi.digitraffic.meri.controller.reader;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.AisMessageConverter;
import fi.livi.digitraffic.meri.controller.CachedLocker;
import fi.livi.digitraffic.meri.controller.VesselMqttSender;
import fi.livi.digitraffic.meri.controller.ais.AisRadioMsg;
import fi.livi.digitraffic.meri.model.ais.AISMessage;
import fi.livi.digitraffic.meri.service.ais.VesselLocationConverter;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.reader.enabled")
public class VesselLocationRelayListener implements AisMessageListener {
    private final VesselMqttSender vesselSender;
    private final CachedLocker aisCachedLocker;

    public VesselLocationRelayListener(final VesselMqttSender vesselSender,
                                       final CachedLocker aisCachedLocker) {
        this.vesselSender = vesselSender;
        this.aisCachedLocker = aisCachedLocker;
    }

    @Override
    public void receiveMessage(final AisRadioMsg message) {
        if (message.isMmsiAllowed() && aisCachedLocker.hasLock()) {
            final AISMessage ais = AisMessageConverter.convertLocation(message);

            if (ais.validate()) {
                final boolean sendStatus = vesselSender.sendLocationMessage(VesselLocationConverter.convert(ais));

                VesselLoggingListener.sentAisMessagesStatistics(VesselLoggingListener.AISLoggingType.POSITION, sendStatus);
            }
        }
    }
}
