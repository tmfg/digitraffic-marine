package fi.livi.digitraffic.meri.controller.websocket;

import static fi.livi.digitraffic.meri.controller.websocket.WebsocketStatistics.UNDEFINED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.VesselSender;
import fi.livi.digitraffic.meri.util.service.LockingService;

@Component
@ConditionalOnProperty("ais.websocketRead.enabled")
public class WebsocketReadStateNotifier {
    private final WebsocketStatistics websocketStatistics;
    private final VesselSender vesselSender;

    private final LockingService lockingService;

    private static final Logger log = LoggerFactory.getLogger(WebsocketReadStateNotifier.class);

    public WebsocketReadStateNotifier(final WebsocketStatistics websocketStatistics, final VesselSender vesselSender,
        final LockingService lockingService) {
        this.websocketStatistics = websocketStatistics;
        this.vesselSender = vesselSender;
        this.lockingService = lockingService;
    }

    @Scheduled(fixedRate = 10000)
    public void sendReadStatus() {
        if(lockingService.hasLockForAis()) {
            try {
                final WebsocketStatistics.ReadStatistics locationStatus = websocketStatistics.getReadStatistics();
                final String status = locationStatus == null ? UNDEFINED : locationStatus.status;

                vesselSender.sendStatusMessage(status);
            } catch (final Exception e) {
                log.info("Exception notifying", e);
            }
        }
    }
}
