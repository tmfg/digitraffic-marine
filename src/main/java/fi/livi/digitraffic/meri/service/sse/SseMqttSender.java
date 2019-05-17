package fi.livi.digitraffic.meri.service.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.config.MqttConfig.SynchronizedMqttGateway;
import fi.livi.digitraffic.meri.model.sse.SseFeature;

@ConditionalOnProperty("sse.mqtt.enabled")
@Component
public class SseMqttSender {
    private static final Logger LOG = LoggerFactory.getLogger(SseMqttSender.class);

    private final SynchronizedMqttGateway mqttGateway;
    private final ObjectMapper objectMapper;

    private static final String SSE_DATA_TOPIC = "sse/site/%d";
    private static final String SSE_STATUS_TOPIC  ="sse/status";

    @Autowired
    public SseMqttSender(final SynchronizedMqttGateway mqttGateway,
                         final ObjectMapper objectMapper) {
        this.mqttGateway = mqttGateway;
        this.objectMapper = objectMapper;
    }

    public boolean sendSseMessage(final SseFeature sseData) {
        try {
            final String sseAsString = objectMapper.writeValueAsString(sseData);

            mqttGateway.sendToMqtt(String.format(SSE_DATA_TOPIC, sseData.getSiteNumber()), sseAsString);

            return true;
        } catch (final Exception e) {
            LOG.error("error sending sse data", e);
        }

        return false;
    }

    public boolean sendStatusMessage(final Object status) {
        try {
            final String statusAsString = objectMapper.writeValueAsString(status);

            mqttGateway.sendToMqtt(SSE_STATUS_TOPIC, statusAsString);

            return true;
        } catch (final Exception e) {
            LOG.error("error sending status", e);
        }

        return false;
    }
}
