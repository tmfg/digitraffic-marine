package fi.livi.digitraffic.meri.service.portnet;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fi.livi.digitraffic.meri.portnet.xsd.PortCallList;
import fi.livi.digitraffic.util.web.Jax2bRestTemplate;

@Service
public class PortCallClient {
    private final String portCallUrl;
    private final Jax2bRestTemplate restTemplate;

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");

    private static final Logger log = LoggerFactory.getLogger(PortCallClient.class);

    @Autowired
    public PortCallClient(@Value("${ais.portnet.portcall.url}") final String portCallUrl,
                          final Jax2bRestTemplate restTemplate) {
        this.portCallUrl = portCallUrl;
        this.restTemplate = restTemplate;
    }

    public PortCallList getList(final Instant lastUpdated, final Instant now) {
        final String url = buildUrl(lastUpdated, now);

        log.info("Fetching port calls from {}", url);

        final PortCallList portCallList = restTemplate.getForObject(url, PortCallList.class);

        logInfo(portCallList);

        return portCallList;
    }

    public static String dateToString(final String datePrefix, final ZonedDateTime timestamp) {
        return String.format("%s=%s", datePrefix, timestamp.format(DATE_FORMATTER));
    }

    public static String timeToString(final String timePrefix, final ZonedDateTime timestamp) {
        return timestamp == null ? "" : String.format("%s=%s", timePrefix, timestamp.format(TIME_FORMATTER));
    }

    private static void logInfo(final PortCallList portCallList) {
        log.info("Number of received notifications: {}", portCallList.getPortCallNotification().size());
    }

    private String buildUrl(final Instant lastUpdated, final Instant now) {
        final ZonedDateTime lastUpdatedDt = lastUpdated.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault());
        final String dateStartParameter = dateToString("startDte", lastUpdatedDt);
        final String timeStartParameter = timeToString("startTme", lastUpdatedDt);
        final String dateEndParameter = dateToString("endDte", now.atZone(ZoneId.systemDefault()));
        final String timeEndParameter = timeToString("endTme", now.atZone(ZoneId.systemDefault()));

        // order is important, must be startDte,endDte,startTme,endTme, otherwise 404

        return String.format("%s%s&%s&%s&%s", portCallUrl, dateStartParameter, dateEndParameter, timeStartParameter, timeEndParameter);
    }
}
