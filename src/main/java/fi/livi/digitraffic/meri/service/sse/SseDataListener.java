package fi.livi.digitraffic.meri.service.sse;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.CachedLocker;
import fi.livi.digitraffic.meri.model.sse.SseFeature;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("sse.mqtt.enabled")
public class SseDataListener {
    private final SseMqttSender sseMqttSender;
    private final CachedLocker sseCachedLocker;

    public SseDataListener(final SseMqttSender sseMqttSender,
                           final CachedLocker sseCachedLocker) {
        this.sseMqttSender = sseMqttSender;
        this.sseCachedLocker = sseCachedLocker;
    }

    public void receiveMessage(final SseFeature message) {
        if (sseCachedLocker.hasLock()) {
            final boolean sendStatus = sseMqttSender.sendSseMessage(message);
            SseLoggingListener.addSendSseMessagesStatistics(sendStatus);
        }
    }
}