package fi.livi.digitraffic.meri.service.sse;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.livi.digitraffic.meri.controller.CachedLocker;
import fi.livi.digitraffic.meri.model.sse.SseFeature;
import fi.livi.digitraffic.meri.mqtt.MqttDataMessageV2;
import fi.livi.digitraffic.meri.mqtt.MqttMessageSender;
import fi.livi.digitraffic.meri.service.MqttRelayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@ConditionalOnProperty("sse.mqtt.enabled")
@Component
public class SseMqttSender {
    private static final Logger LOG = LoggerFactory.getLogger(SseMqttSender.class);

    private final MqttMessageSender mqttMessageSender;

    private static final String SSE_DATA_TOPIC = "sse/site/%d";
    private static final String SSE_STATUS_TOPIC  ="sse/status";

    public SseMqttSender(final MqttRelayQueue mqttRelayQueue,
                         final ObjectMapper objectMapper,
                         final CachedLocker sseCachedLocker) {
        this.mqttMessageSender = new MqttMessageSender(LOG, mqttRelayQueue, objectMapper, MqttRelayQueue.StatisticsType.SSE, sseCachedLocker);
    }

    public boolean sendSseMessage(final SseFeature sseData) {
        try {
            mqttMessageSender.sendMqttMessage(sseData.getProperties().getLastUpdate().atZone(ZoneId.of("UTC")),
                new MqttDataMessageV2(SSE_DATA_TOPIC, sseData));
                //.sendToMqtt(String.format(SSE_DATA_TOPIC, sseData.getSiteNumber()), sseAsString);

            return true;
        } catch (final Exception e) {
            LOG.error("error sending sse data", e);
        }

        return false;
    }

    public boolean sendStatusMessage(final Object status) {
        try {
            mqttMessageSender.sendStatusMessageV1(SSE_STATUS_TOPIC, status);

            return true;
        } catch (final Exception e) {
            LOG.error("error sending status", e);
        }

        return false;
    }
}
