package fi.livi.digitraffic.meri.service.sse;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.livi.digitraffic.meri.controller.CachedLocker;
import fi.livi.digitraffic.meri.model.geojson.Feature;
import fi.livi.digitraffic.meri.model.sse.SseFeature;
import fi.livi.digitraffic.meri.model.sse.SseFeatureCollection;
import fi.livi.digitraffic.meri.mqtt.MqttDataMessageV2;
import fi.livi.digitraffic.meri.mqtt.MqttMessageSender;
import fi.livi.digitraffic.meri.service.MqttRelayQueue;
import fi.livi.digitraffic.meri.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static fi.livi.digitraffic.meri.util.MqttUtil.getTopicForMessage;

@ConditionalOnProperty("sse.mqtt.enabled")
@ConditionalOnNotWebApplication
@Component
public class SseMqttSenderV1 {
    private final MqttMessageSender mqttMessageSender;
    private final SseService sseService;

    private static final String SSE_V1_DATA_TOPIC = "sse/site/%d";
    private static final String SSE_V1_STATUS_TOPIC  ="sse/status";

    private static final Logger LOG = LoggerFactory.getLogger(SseMqttSenderV1.class);

    public SseMqttSenderV1(final MqttRelayQueue mqttRelayQueue,
                           final ObjectMapper objectMapper,
                           final CachedLocker sseCachedLocker,
                           final SseService sseService) {
        this.mqttMessageSender = new MqttMessageSender(LOG, mqttRelayQueue, objectMapper, MqttRelayQueue.StatisticsType.SSE, sseCachedLocker);
        this.sseService = sseService;

        this.mqttMessageSender.setLastUpdated(ZonedDateTime.now());
    }

    @Scheduled(fixedRate = 10000)
    public void checkNewSseReports() {
        if (mqttMessageSender.hasLock()) {
            final List<SseFeature> features = sseService.findCreatedAfter(mqttMessageSender.getLastUpdated().toInstant()).getFeatures();

            if (!features.isEmpty()) {
                features.forEach(sseFeature -> {
                    mqttMessageSender.sendMqttMessage(sseFeature.getProperties().getCreated().atZone(ZoneId.of("UTC")),
                        new MqttDataMessageV2(getTopicForMessage(SSE_V1_DATA_TOPIC, sseFeature.getProperties().siteNumber), sseFeature));

                    SseLoggingListener.addSendSseMessagesStatistics(true);
                });
            }
        }
    }

    public boolean sendStatusMessage(final SseLoggingListener.Statistics statusMessage) {
        return mqttMessageSender.sendStatusMessageV1(SSE_V1_STATUS_TOPIC, statusMessage);
    }
}
