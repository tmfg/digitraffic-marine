package fi.livi.digitraffic.meri.service.portnet.call;

import static fi.livi.digitraffic.common.util.TimeUtil.FINLAND_ZONE;
import static fi.livi.digitraffic.common.util.TimeUtil.dateToString;
import static fi.livi.digitraffic.common.util.TimeUtil.timeToString;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import fi.livi.digitraffic.common.annotation.NotTransactionalServiceMethod;
import fi.livi.digitraffic.common.util.StringUtil;
import fi.livi.digitraffic.meri.portnet.xsd.PortCallList;

@Service
@ConditionalOnNotWebApplication
public class PortCallClient {
    private final String portCallUrl;
    private final WebClient portnetWebClient;

    private static final Logger log = LoggerFactory.getLogger(PortCallClient.class);

    public PortCallClient(@Value("${dt.portnet.portcall.url}") final String portCallUrl,
                          final WebClient portnetWebClient) {
        this.portCallUrl = portCallUrl;
        this.portnetWebClient = portnetWebClient;
    }

    @NotTransactionalServiceMethod
    @Retryable
    public PortCallList getList(final ZonedDateTime lastUpdated, final ZonedDateTime now) {
        final String url = buildUrl(lastUpdated, now);

        log.info("Fetching port calls from url={}", url);

        final PortCallList portCallList = portnetWebClient
            .mutate().baseUrl(url).build()
            .get().retrieve()
            .bodyToMono(PortCallList.class).block();

        logInfo(portCallList);

        if (log.isDebugEnabled()) {
            log.debug("method=getList url={} portCallList={}",
                url, StringUtil.toJsonStringLogSafe(portCallList));
        }

        return portCallList;
    }

    private static void logInfo(final PortCallList portCallList) {
        if(portCallList == null) {
            log.info("Empty portcalllist received! notificationsCount=0");
        } else {
            log.info("Number of received notificationsCount={}", portCallList.getPortCallNotification().size());
        }
    }

    private String buildUrl(final ZonedDateTime lastUpdated, final ZonedDateTime now) {
        // calls must be in Helsinki time!
        final ZonedDateTime start = lastUpdated.withZoneSameInstant(FINLAND_ZONE);
        final ZonedDateTime end = now.withZoneSameInstant(FINLAND_ZONE);

        final String dateStartParameter = dateToString("startDte", start);
        final String timeStartParameter = timeToString("startTme", start);
        final String dateEndParameter = dateToString("endDte", end);
        final String timeEndParameter = timeToString("endTme", end);

        // order is important, must be startDte,endDte,startTme,endTme, otherwise 404
        return String.format("%s%s&%s&%s&%s", portCallUrl, dateStartParameter, dateEndParameter, timeStartParameter, timeEndParameter);
    }
}
