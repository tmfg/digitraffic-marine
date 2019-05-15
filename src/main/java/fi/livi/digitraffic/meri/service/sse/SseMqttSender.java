package fi.livi.digitraffic.meri.service.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.config.MqttConfig;
import fi.livi.digitraffic.meri.model.sse.SseFeature;

@ConditionalOnProperty("sse.mqtt.enabled")
@Component
public class SseMqttSender {
    private static final Logger LOG = LoggerFactory.getLogger(SseMqttSender.class);

    private final MqttConfig.SseGateway sseGateway;
    private final ObjectMapper objectMapper;

    private static final String SSE_DATA_TOPIC = "sse/site/%d";
    private static final String SSE_STATUS_TOPIC  ="sse/status";

    @Autowired
    public SseMqttSender(final MqttConfig.SseGateway sseGateway,
                         final ObjectMapper objectMapper) {
        this.sseGateway = sseGateway;
        this.objectMapper = objectMapper;
    }

    public boolean sendSseMessage(final SseFeature sseData) {
        try {
            final String sseAsString = objectMapper.writeValueAsString(sseData);

            sendMessage(sseAsString, String.format(SSE_DATA_TOPIC, sseData.getSiteNumber()));

            return true;
        } catch (final Exception e) {
            LOG.error("error sending sse data", e);
        }

        return false;
    }

    public boolean sendStatusMessage(final Object status) {
        try {
            final String statusAsString = objectMapper.writeValueAsString(status);

            sendMessage(statusAsString, SSE_STATUS_TOPIC);

            return true;
        } catch (final Exception e) {
            LOG.error("error sending status", e);
        }

        return false;
    }

    // This must be synchronized, because Paho does not support concurrency!
    private synchronized void sendMessage(final String payLoad, final String topic) {
        sseGateway.sendToMqtt(topic, payLoad);
    }
}
