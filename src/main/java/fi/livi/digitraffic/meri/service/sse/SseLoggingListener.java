package fi.livi.digitraffic.meri.service.sse;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.CachedLocker;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("sse.mqtt.enabled")
public class SseLoggingListener {
    private static final Logger log = LoggerFactory.getLogger(SseLoggingListener.class);

    private static final Map<SseLoggingType, Statistics> sentStatisticsMap = new ConcurrentHashMap<>();

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private final CachedLocker sseCachedLocker;
    private final SseMqttSender sseMqttSender;

    public SseLoggingListener(final CachedLocker sseCachedLocker,
                              final SseMqttSender sseMqttSender) {
        this.sseCachedLocker = sseCachedLocker;
        this.sseMqttSender = sseMqttSender;

        executor.scheduleAtFixedRate(this::sendStatusAndLogSentStatistics, 30, 60, TimeUnit.SECONDS);
    }

    public enum SseLoggingType {
        DATA, STATUS
    }

    @PreDestroy
    public void destroy() {
        executor.shutdown();
    }

    public static void addSendSseMessagesStatistics(final boolean sendSuccessful) {
        addStatistics(SseLoggingType.DATA, sendSuccessful);
    }

    public static void addSendStatusMessagesStatistics(final boolean sendSuccessful) {
        addStatistics(SseLoggingType.STATUS, sendSuccessful);
    }

    private static synchronized void addStatistics(final SseLoggingType loggingType, final boolean sendSuccessful) {
        final Statistics statistics = sentStatisticsMap.get(loggingType);
        int messages = sendSuccessful ? 1 : 0;
        int failures = sendSuccessful ? 0 : 1;

        final Statistics newStat = (statistics == null) ?
            new Statistics(messages, failures) :
            new Statistics(statistics.messages + messages, statistics.failures + failures);

        sentStatisticsMap.put(loggingType, newStat);
    }

    private synchronized void sendStatusAndLogSentStatistics() {
        sendStatus();
        logSentStatistics();
    }

    private void logSentStatistics() {
        for (final SseLoggingType sseMessageType : Arrays.asList(SseLoggingType.DATA, SseLoggingType.STATUS)) {
            final Statistics sentStatistics = sentStatisticsMap.get(sseMessageType);

            log.info("SSE-message statistics for messageType={} messages={} failures={}",
                sseMessageType,
                sentStatistics != null ? sentStatistics.messages : 0,
                sentStatistics != null ? sentStatistics.failures : 0);

            sentStatisticsMap.put(sseMessageType, new Statistics(0, 0));
        }
    }

    private void sendStatus() {
        if (sseCachedLocker.hasLock()) {

            int sendCount = 0;
            int errorCount = 0;

            for (final SseLoggingType sseMessageType : Arrays.asList(SseLoggingType.DATA, SseLoggingType.STATUS)) {
                final Statistics sentStatistics = sentStatisticsMap.get(sseMessageType);

                if (sentStatistics != null) {
                    sendCount += sentStatistics.messages;
                }
                if (sentStatistics != null) {
                    errorCount += sentStatistics.failures;
                }
            }

            final Statistics statusMessage = new StatusMessage(sendCount+1, errorCount);

            try {
                boolean sendStatus = sseMqttSender.sendStatusMessage(statusMessage);
                addSendStatusMessagesStatistics(sendStatus);
            } catch (final Exception e) {
                log.error("Json parse error", e);
            }
        }
    }

    private static class Statistics {
        final int messages;
        final int failures;

        private Statistics(final int messages, final int failures) {
            this.messages = messages;
            this.failures = failures;
        }

        public int getMessages() {
            return messages;
        }

        public int getFailures() {
            return failures;
        }
    }

    protected static class StatusMessage extends Statistics {
        private final ZonedDateTime timeStamp;

        public StatusMessage(final int messages, final int failures) {
            super(messages, failures);
            this.timeStamp = Instant.now().atZone(ZoneOffset.UTC);
        }

        public ZonedDateTime getTimeStamp() {
            return timeStamp;
        }
    }

}
