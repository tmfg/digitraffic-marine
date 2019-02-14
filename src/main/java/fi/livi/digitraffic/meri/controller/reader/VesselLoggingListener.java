package fi.livi.digitraffic.meri.controller.reader;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import fi.livi.digitraffic.meri.controller.AisLocker;
import fi.livi.digitraffic.meri.controller.VesselMqttSender;
import fi.livi.digitraffic.meri.controller.ais.AisRadioMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.reader.enabled")
public class VesselLoggingListener implements AisMessageListener {
    private static final Logger log = LoggerFactory.getLogger(VesselLoggingListener.class);

    private static final Map<AISMessageType, SentStatistics> sentStatisticsMap = new ConcurrentHashMap<>();
    private static final Map<AISMessageType, ReadStatistics> readStatisticsMap = new ConcurrentHashMap<>();

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private final AisLocker aisLocker;

    public VesselLoggingListener(final AisLocker aisLocker, final VesselMqttSender vesselSender) {
        this.aisLocker = aisLocker;

        executor.scheduleAtFixedRate(this::logReadStatistics, 20, 60, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(this::logSentStatistics, 20, 60, TimeUnit.SECONDS);
        //executor.scheduleAtFixedRate(this::readStatus, 20, 10, TimeUnit.SECONDS);
    }

    public enum AISMessageType {
        POSITION, METADATA
    }

    @PreDestroy
    public void destroy() {
        executor.shutdown();
    }

    @Override
    public void receiveMessage(AisRadioMsg message) {
        readAisMessagesStatistics(message.isPositionMsg() ? AISMessageType.POSITION : AISMessageType.METADATA, 1, AisTcpSocketClient.ConnectionStatus.CONNECTED);
    }

    public static synchronized void readAisMessagesStatistics(final AISMessageType type, final int messageCount, final AisTcpSocketClient.ConnectionStatus connectionStatus) {
        final ReadStatistics rs = readStatisticsMap.get(type);
        final int problem = connectionStatus == AisTcpSocketClient.ConnectionStatus.CONNECT_FAILURE ? 1 : 0;

        final ReadStatistics newRs = rs == null ?
            new ReadStatistics(messageCount, connectionStatus, problem) :
            new ReadStatistics(rs.messages + messageCount, connectionStatus, rs.readProblems + problem);

        readStatisticsMap.put(type, newRs);
    }

    public static synchronized void readAisMessagesStatus(final AisTcpSocketClient.ConnectionStatus connectionStatus) {
        readAisMessagesStatistics(AISMessageType.POSITION, 0, connectionStatus);
        readAisMessagesStatistics(AISMessageType.METADATA, 0, connectionStatus);
    }

    public static synchronized void sentAisMessagesStatistics(final AISMessageType type, final int sent, final int filtered) {
        final SentStatistics ss = sentStatisticsMap.get(type);

        final SentStatistics newSs = ss == null ?
            new SentStatistics(sent, filtered) :
            new SentStatistics(ss.messages + sent, ss.filtered + filtered);

        sentStatisticsMap.put(type, newSs);
    }

    private synchronized void logReadStatistics() {
        for (final AISMessageType aisMessageType : Arrays.asList(AISMessageType.POSITION, AISMessageType.METADATA)) {
            final ReadStatistics readStatistics = readStatisticsMap.get(aisMessageType);

            log.info("Read ais-message statistics for messageType={} messages={} status={} connectionProblems={}",
                aisMessageType,
                readStatistics != null ? readStatistics.messages : 0,
                readStatistics != null ? readStatistics.status.toString() : AisTcpSocketClient.ConnectionStatus.UNDEFINED.toString(),
                readStatistics != null ? readStatistics.readProblems : 0
            );

            readStatisticsMap.put(aisMessageType, new ReadStatistics(0, readStatistics != null ? readStatistics.status : AisTcpSocketClient.ConnectionStatus.UNDEFINED, 0));
        }
    }

    private synchronized void logSentStatistics() {
        for (final AISMessageType aisMessageType : Arrays.asList(AISMessageType.POSITION, AISMessageType.METADATA)) {
            final SentStatistics sentStatistics = sentStatisticsMap.get(aisMessageType);
            log.info("Sent ais-message statistics for messageType={} messages={} filtered={}",
                aisMessageType,
                sentStatistics != null ? sentStatistics.messages : 0,
                sentStatistics != null ? sentStatistics.filtered : 0);

            sentStatisticsMap.put(aisMessageType, new SentStatistics(0, 0));
        }
    }

    private synchronized void readStatus() {
        if (aisLocker.hasLockForAis()) {
            try {

            } catch (Exception e) {
                log.info("Exception notifying", e);
            }
        }
    }

    private static class SentStatistics {
        final int messages;
        final int filtered;

        private SentStatistics(final int messages, final int filtered) {
            this.messages = messages;
            this.filtered = filtered;
        }
    }

    private static class ReadStatistics {
        final int messages;
        final AisTcpSocketClient.ConnectionStatus status;
        final int readProblems;

        private ReadStatistics(final int messages, final AisTcpSocketClient.ConnectionStatus status, final int readProblems) {
            this.messages = messages;
            this.status = status;
            this.readProblems = readProblems;
        }
    }
}
