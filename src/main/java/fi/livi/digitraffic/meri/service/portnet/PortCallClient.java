package fi.livi.digitraffic.meri.service.portnet;

import static fi.livi.digitraffic.meri.util.TimeUtil.FINLAND_ZONE;
import static fi.livi.digitraffic.meri.util.TimeUtil.dateToString;
import static fi.livi.digitraffic.meri.util.TimeUtil.timeToString;

import java.time.ZonedDateTime;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import fi.livi.digitraffic.meri.annotation.NotTransactionalServiceMethod;
import fi.livi.digitraffic.meri.portnet.xsd.PortCallList;
import fi.livi.digitraffic.meri.util.StringUtil;

@Service
@ConditionalOnNotWebApplication
public class PortCallClient {
    private final String portCallUrl;
    private final RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(PortCallClient.class);

    @Autowired
    public PortCallClient(@Value("${dt.portnet.portcall.url}") final String portCallUrl,
                          final RestTemplate authenticatedRestTemplate) {
        this.portCallUrl = portCallUrl;
        this.restTemplate = authenticatedRestTemplate;
        this.restTemplate.setMessageConverters(Collections.singletonList(new Jaxb2RootElementHttpMessageConverter()));
    }

    @NotTransactionalServiceMethod
    @Retryable
    public PortCallList getList(final ZonedDateTime lastUpdated, final ZonedDateTime now) {
        final String url = buildUrl(lastUpdated, now);

        log.info("Fetching port calls from url={}", url);

        final PortCallList portCallList = restTemplate.getForObject(url, PortCallList.class);

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
