package fi.livi.digitraffic.meri.service.sse;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.livi.digitraffic.meri.controller.CachedLocker;
import fi.livi.digitraffic.meri.model.sse.SseFeature;
import fi.livi.digitraffic.meri.mqtt.MqttDataMessageV2;
import fi.livi.digitraffic.meri.mqtt.MqttMessageSender;
import fi.livi.digitraffic.meri.mqtt.MqttSseMessageV2;
import fi.livi.digitraffic.meri.service.MqttRelayQueue;
import fi.livi.digitraffic.meri.util.MqttUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@ConditionalOnProperty("sse.mqtt.enabled")
@ConditionalOnNotWebApplication
@Component
public class SseMqttSenderV2 {
    private final MqttMessageSender mqttMessageSender;
    private final SseService_V1 sseServiceV1;

    private static final String SSE_V2_DATA_TOPIC = "sse-v2/site/%d";
    private static final String SSE_V2_STATUS_TOPIC ="sse-v2/status";

    private static final Logger LOG = LoggerFactory.getLogger(SseMqttSenderV2.class);

    public SseMqttSenderV2(final MqttRelayQueue mqttRelayQueue,
                           final ObjectMapper objectMapper,
                           final CachedLocker sseCachedLocker,
                           final SseService_V1 sseServiceV1) {
        this.mqttMessageSender = new MqttMessageSender(LOG, mqttRelayQueue, objectMapper, MqttRelayQueue.StatisticsType.SSE, sseCachedLocker);
        this.sseServiceV1 = sseServiceV1;

        this.mqttMessageSender.setLastUpdated(ZonedDateTime.now());
    }

    @Scheduled(fixedRate = 10000)
    public void checkNewSseReports() {
        if (mqttMessageSender.hasLock()) {
            final List<SseFeature> features = sseServiceV1.findCreatedAfter(mqttMessageSender.getLastUpdated().toInstant()).getFeatures();

            if (!features.isEmpty()) {
                features.forEach(sseFeature -> {
                    mqttMessageSender.sendMqttMessage(sseFeature.getProperties().getCreated().atZone(ZoneId.of("UTC")),
                        createMqttDataMessage(sseFeature));

                    SseLoggingListener.addSendSseMessagesStatistics(true);
                });
            }
        }
    }

    public static MqttDataMessageV2 createMqttDataMessage(final SseFeature feature) {
        final String topic = MqttUtil.getTopicForMessage(SSE_V2_DATA_TOPIC, feature.getProperties().siteNumber);

        return new MqttDataMessageV2(topic, new MqttSseMessageV2(feature));
    }

    @Scheduled(fixedDelayString = "${mqtt.status.intervalMs}")
    public void sendStatusMessage() {
        if (mqttMessageSender.hasLock()) {
            mqttMessageSender.sendStatusMessageV2(SSE_V2_STATUS_TOPIC);
        }
    }
}
