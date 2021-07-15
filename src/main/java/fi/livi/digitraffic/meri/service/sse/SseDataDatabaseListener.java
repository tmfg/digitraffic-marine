package fi.livi.digitraffic.meri.service.sse;

import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.CachedLocker;
import fi.livi.digitraffic.meri.model.sse.SseFeatureCollection;
import fi.livi.digitraffic.meri.util.TimeUtil;

@Component
@ConditionalOnProperty("sse.mqtt.enabled")
public class SseDataDatabaseListener {

    private final SseMqttSender sseMqttSender;
    private final CachedLocker sseCachedLocker;
    private SseService sseService;

    private Instant latest;

    public SseDataDatabaseListener(final SseMqttSender sseMqttSender,
                                   final CachedLocker sseCachedLocker,
                                   final SseService sseService) {
        this.sseMqttSender = sseMqttSender;
        this.sseCachedLocker = sseCachedLocker;
        this.sseService = sseService;
        this.latest = TimeUtil.withoutMillis(Instant.now());
    }

    @Scheduled(fixedRate = 1000)
    public void checkNewSseReports() {
        if (!sseCachedLocker.hasLock()) {
            return;
        }

        final SseFeatureCollection history = sseService.findCreatedAfter(latest);
        if (history.getFeatures().isEmpty()) {
            return;
        }

        history.getFeatures().forEach(sseFeature -> {
            final boolean sendStatus = sseMqttSender.sendSseMessage(sseFeature);
            SseLoggingListener.addSendSseMessagesStatistics(sendStatus);
        });

        final Optional<Instant> max =
            history.getFeatures().stream().map(s -> s.getProperties().getCreated()).max(Instant::compareTo);
        if (max.isPresent()) {
            latest = max.get();
        }
    }
}