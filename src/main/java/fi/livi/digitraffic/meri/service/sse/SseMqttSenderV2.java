package fi.livi.digitraffic.meri.service.sse;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.common.service.locking.CachedLockingService;
import fi.livi.digitraffic.common.service.locking.LockingService;
import fi.livi.digitraffic.common.util.MqttUtil;
import fi.livi.digitraffic.meri.dto.mqtt.MqttDataMessageV2;
import fi.livi.digitraffic.meri.dto.mqtt.MqttMessageSender;
import fi.livi.digitraffic.meri.dto.mqtt.MqttSseMessageV2;
import fi.livi.digitraffic.meri.dto.sse.v1.SseFeatureV1;
import fi.livi.digitraffic.meri.service.MqttRelayQueue;

@ConditionalOnProperty("sse.mqtt.enabled")
@ConditionalOnNotWebApplication
@Component
public class SseMqttSenderV2 {
    private final MqttMessageSender mqttMessageSender;
    private final SseDaemonService sseWebServiceV1;

    private static final String SSE_V2_DATA_TOPIC = "sse-v2/site/%d";
    private static final String SSE_V2_STATUS_TOPIC ="sse-v2/status";

    private static final Logger LOG = LoggerFactory.getLogger(SseMqttSenderV2.class);

    public SseMqttSenderV2(final MqttRelayQueue mqttRelayQueue,
                           final ObjectMapper objectMapper,
                           final LockingService lockingService,
                           final SseDaemonService sseWebServiceV1) {
        final CachedLockingService sseCachedLockingService =
            lockingService.createCachedLockingService("SSE_LOCK");
        this.mqttMessageSender = new MqttMessageSender(LOG, mqttRelayQueue, objectMapper, MqttRelayQueue.StatisticsType.SSE, sseCachedLockingService);
        this.sseWebServiceV1 = sseWebServiceV1;

        this.mqttMessageSender.setLastUpdated(Instant.now());
    }

    @Scheduled(fixedRate = 10000)
    public void checkNewSseReports() {
        if (mqttMessageSender.hasLock()) {
            final List<SseFeatureV1> features = sseWebServiceV1.findCreatedAfter(mqttMessageSender.getLastUpdated()).getFeatures();

            if (!features.isEmpty()) {
                features.forEach(sseFeature ->
                        SseLoggingListener.addSendSseMessagesStatistics(
                            mqttMessageSender.sendMqttMessage(sseFeature.getProperties().getCreated(),
                                                              createMqttDataMessage(sseFeature))
                        ));
            }
        }
    }

    public static MqttDataMessageV2 createMqttDataMessage(final SseFeatureV1 feature) {
        final String topic = MqttUtil.getTopicForMessage(SSE_V2_DATA_TOPIC, feature.getProperties().siteNumber);

        return new MqttDataMessageV2(topic, new MqttSseMessageV2(feature));
    }

    @Scheduled(fixedDelayString = "${mqtt.status.intervalMs}")
    public void sendStatusMessage() {
        if (mqttMessageSender.hasLock()) {
            SseLoggingListener.addSendStatusMessagesStatistics(
                    mqttMessageSender.sendStatusMessageV2(SSE_V2_STATUS_TOPIC)
            );
        }
    }
}
