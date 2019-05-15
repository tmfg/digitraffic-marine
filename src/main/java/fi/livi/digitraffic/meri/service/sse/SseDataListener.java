package fi.livi.digitraffic.meri.service.sse;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.AisLocker;
import fi.livi.digitraffic.meri.model.sse.SseFeature;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("sse.mqtt.enabled")
public class SseDataListener {
    private final SseMqttSender sseMqttSender;
    private final AisLocker aisLocker;

    public SseDataListener(final SseMqttSender sseMqttSender,
                           final AisLocker aisLocker) {
        this.sseMqttSender = sseMqttSender;
        this.aisLocker = aisLocker;
    }

    public void receiveMessage(final SseFeature message) {
        if (aisLocker.hasLockForAis()) {
            final boolean sendStatus = sseMqttSender.sendSseMessage(message);
            SseLoggingListener.addSendSseMessagesStatistics(sendStatus);
        }
    }
}