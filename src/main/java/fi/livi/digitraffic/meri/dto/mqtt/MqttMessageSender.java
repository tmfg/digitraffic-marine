package fi.livi.digitraffic.meri.dto.mqtt;

import static fi.livi.digitraffic.meri.service.MqttRelayQueue.StatisticsType.STATUS;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.common.util.TimeUtil;
import fi.livi.digitraffic.meri.service.CachedLockerService;
import fi.livi.digitraffic.meri.service.MqttRelayQueue;

public class MqttMessageSender {
    private final Logger log;

    private final MqttRelayQueue mqttRelayQueue;
    private final ObjectMapper objectMapper;

    private final CachedLockerService cachedLocker;

    private final AtomicReference<Instant> lastUpdated = new AtomicReference<>();
    private final AtomicReference<Instant> lastError = new AtomicReference<>();
    private final MqttRelayQueue.StatisticsType statisticsType;

    public MqttMessageSender(final Logger log,
                             final MqttRelayQueue mqttRelayQueue,
                             final ObjectMapper objectMapper,
                             final MqttRelayQueue.StatisticsType statisticsType,
                             final CachedLockerService cachedLocker) {
        this.log = log;
        this.mqttRelayQueue = mqttRelayQueue;
        this.objectMapper = objectMapper;
        this.statisticsType = statisticsType;

        this.cachedLocker = cachedLocker;
    }

    public boolean sendMqttMessage(final Instant lastUpdated, final MqttDataMessageV2 message) {
        // Get lock and keep it to prevent sending on multiple nodes

        boolean success = true; // When lock is not acquired, it is not error -> default success == true :)
        if (hasLock()) {
            success = doSendMqttMessage(message);
        }

        if(lastUpdated != null) {
            setLastUpdated(lastUpdated);
        }
        return success;
    }

    public boolean hasLock() {
        return cachedLocker.hasLock();
    }

    private boolean doSendMqttMessage(final MqttDataMessageV2 message) {
        try {
            log.debug("method=sendMqttMessage {}", message);
            mqttRelayQueue.queueMqttMessage(message.getTopic(), objectMapper.writeValueAsString(message.getData()), statisticsType);
        } catch (final JsonProcessingException e) {
            setLastError(Instant.now());
            log.error("method=sendMqttMessage Error sending message", e);
            return false;
        }
        return true;
    }

    public void setLastUpdated(final Instant lastUpdatedIn) {
        lastUpdated.set(Objects.requireNonNullElse(lastUpdatedIn, Instant.now()));
    }

    public Instant getLastUpdated() {
        return lastUpdated.get();
    }

    private void setLastError(final Instant lastErrorIn) {
        lastError.set(lastErrorIn);
    }

    private Instant getLastError() {
        return lastError.get();
    }

    public boolean sendStatusMessageV2(final String statusTopic) {
        try {
            final MqttStatusMessageV2 message =
                new MqttStatusMessageV2(TimeUtil.getEpochSeconds(getLastUpdated()), TimeUtil.getEpochSeconds(getLastError()));

            mqttRelayQueue.queueMqttMessage(statusTopic, objectMapper.writeValueAsString(message), STATUS);
        } catch (final Exception e) {
            log.error("method=sendStatus Error sending message", e);

            return false;
        }

        return true;
    }
}
