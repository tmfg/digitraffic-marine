package fi.livi.digitraffic.meri.service.portnet;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import fi.livi.digitraffic.meri.portnet.xsd.PortCallList;

@Service
public class PortCallClient {
    private final String portCallUrl;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");

    private static final Logger log = LoggerFactory.getLogger(PortCallClient.class);

    public PortCallClient(@Value("${ais.portnet.portcall.url}") final String portCallUrl) {
        this.portCallUrl = portCallUrl;
    }

    public PortCallList getList(final Instant lastUpdated, final Instant now) {
        final RestTemplate template = new RestTemplate();
        final String url = buildUrl(lastUpdated, now);

        log.info("fetching port calls from " + url);

        System.out.println("viesti " + template.getForObject(url, String.class));

        return template.getForObject(url, PortCallList.class);
    }

    private String dateToString(final String datePrefix, final ZonedDateTime timestamp) {
        return String.format("%s=%s", datePrefix, timestamp.format(DATE_FORMATTER));
    }

    private String timeToString(final String timePrefix, final ZonedDateTime timestamp) {
        return timestamp == null ? "" : String.format("%s=%s", timePrefix, timestamp.format(TIME_FORMATTER));
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
