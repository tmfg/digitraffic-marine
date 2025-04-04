package fi.livi.digitraffic.meri.service.portnet.vesseldetails;

import static fi.livi.digitraffic.common.util.TimeUtil.dateToString;
import static fi.livi.digitraffic.common.util.TimeUtil.timeToString;

import java.time.ZonedDateTime;

import fi.livi.digitraffic.common.annotation.NotTransactionalServiceMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import fi.livi.digitraffic.common.util.StringUtil;
import fi.livi.digitraffic.meri.portnet.xsd.VesselList;

@Service
@ConditionalOnNotWebApplication
public class VesselDetailsClient {
    private final String vesselDetailsUrl;
    private final WebClient portnetWebclient;

    private static final Logger log = LoggerFactory.getLogger(VesselDetailsClient.class);

    public VesselDetailsClient(@Value("${dt.portnet.vesselDetails.url}") final String vesselDetailsUrl,
                               final WebClient portnetWebClient) {
        this.vesselDetailsUrl = vesselDetailsUrl;
        this.portnetWebclient = portnetWebClient;
    }

    @NotTransactionalServiceMethod
    public VesselList getVesselList(final ZonedDateTime from) {
        final String url = buildUrl(from);

        return portnetWebclient.mutate().baseUrl(url).build()
            .get().retrieve().bodyToMono(VesselList.class).block();
    }

    private static void logInfo(final VesselList vesselList) {
        log.info("VesselDetailsCount={}", vesselList.getVesselDetails().size());

        log.info(StringUtil.toJsonStringLogSafe(vesselList));
    }

    private String buildUrl(final ZonedDateTime from) {
        final String dateFromParameter = dateToString("fromDte", from);
        final String timeFromParameter = timeToString("fromTme", from);

        return String.format("%s%s&%s", vesselDetailsUrl, dateFromParameter, timeFromParameter);
    }
}
