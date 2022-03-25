package fi.livi.digitraffic.meri.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.livi.digitraffic.meri.controller.CachedLocker;
import fi.livi.digitraffic.meri.model.ais.StatusMessage;
import fi.livi.digitraffic.meri.service.MqttRelayQueue;
import org.slf4j.Logger;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static fi.livi.digitraffic.meri.service.MqttRelayQueue.StatisticsType.STATUS;
import static fi.livi.digitraffic.meri.util.MqttUtil.getEpochSeconds;

public class MqttMessageSender {
    private final Logger log;

    private final MqttRelayQueue mqttRelayQueue;
    private final ObjectMapper objectMapper;

    private final CachedLocker cachedLocker;

    private final AtomicReference<ZonedDateTime> lastUpdated = new AtomicReference<>();
    private final AtomicReference<ZonedDateTime> lastError = new AtomicReference<>();
    private final MqttRelayQueue.StatisticsType statisticsType;

    public MqttMessageSender(final Logger log,
                             final MqttRelayQueue mqttRelayQueue,
                             final ObjectMapper objectMapper,
                             final MqttRelayQueue.StatisticsType statisticsType,
                             final CachedLocker cachedLocker) {
        this.log = log;
        this.mqttRelayQueue = mqttRelayQueue;
        this.objectMapper = objectMapper;
        this.statisticsType = statisticsType;

        this.cachedLocker = cachedLocker;
    }

    public void sendMqttMessage(final ZonedDateTime lastUpdated, final MqttDataMessageV2 message) {
        // Get lock and keep it to prevent sending on multiple nodes

        if (hasLock()) {
            doSendMqttMessage(message);
        }

        if(lastUpdated != null) {
            setLastUpdated(lastUpdated);
        }
    }

    public boolean hasLock() {
        return cachedLocker.hasLock();
    }

    private void doSendMqttMessage(final MqttDataMessageV2 message) {
        try {
            log.debug("method=sendMqttMessage {}", message);
            mqttRelayQueue.queueMqttMessage(message.getTopic(), objectMapper.writeValueAsString(message.getData()), statisticsType);
        } catch (final JsonProcessingException e) {
            setLastError(ZonedDateTime.now());
            log.error("method=sendMqttMessage Error sending message", e);
        }
    }

    public void setLastUpdated(final ZonedDateTime lastUpdatedIn) {
        lastUpdated.set(Objects.requireNonNullElse(lastUpdatedIn, ZonedDateTime.now()));
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated.get();
    }

    private void setLastError(final ZonedDateTime lastErrorIn) {
        lastError.set(lastErrorIn);
    }

    private ZonedDateTime getLastError() {
        return lastError.get();
    }

    public boolean sendStatusMessageV2(final String statusTopic) {
        try {
            final MqttStatusMessageV2 message =
                new MqttStatusMessageV2(getEpochSeconds(getLastUpdated()), getEpochSeconds(getLastError()));

            mqttRelayQueue.queueMqttMessage(statusTopic, objectMapper.writeValueAsString(message), STATUS);
        } catch (final Exception e) {
            log.error("method=sendStatus Error sending message", e);

            return false;
        }

        return true;
    }

    public boolean sendStatusMessageV1(final String topic, final Object status) {
        try {
            final String statusAsString = objectMapper.writeValueAsString(status);

            mqttRelayQueue.queueMqttMessage(topic, statusAsString, STATUS);
        } catch (final Exception e) {
            log.error("error sending status", e);

            return false;
        }

        return true;
    }
}
