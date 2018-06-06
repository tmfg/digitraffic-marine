package fi.livi.digitraffic.meri.controller.websocket;

import static fi.livi.digitraffic.meri.controller.websocket.WebsocketStatistics.UNDEFINED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.VesselSender;

@Component
@ConditionalOnProperty("ais.websocketRead.enabled")
public class WebsocketReadStateNotifier {
    private final WebsocketStatistics websocketStatistics;
    private final VesselSender vesselSender;

    private static final Logger log = LoggerFactory.getLogger(WebsocketReadStateNotifier.class);

    public WebsocketReadStateNotifier(final WebsocketStatistics websocketStatistics, final VesselSender vesselSender) {
        this.websocketStatistics = websocketStatistics;
        this.vesselSender = vesselSender;
    }

    @Scheduled(fixedRate = 10000)
    public void sendReadStatus() {
        log.info("sending status");

        try {
            final WebsocketStatistics.ReadStatistics locationStatus = websocketStatistics.getReadStatistics();
            final String status = locationStatus == null ? UNDEFINED : locationStatus.status;

            vesselSender.sendStatusMessage(status);
        } catch(final Exception e) {
            log.info("Exception notifying", e);
        }
    }
}
