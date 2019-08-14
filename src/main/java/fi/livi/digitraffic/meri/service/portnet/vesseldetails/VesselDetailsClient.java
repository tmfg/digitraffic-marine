package fi.livi.digitraffic.meri.service.portnet.vesseldetails;

import static fi.livi.digitraffic.meri.util.TimeUtil.dateToString;
import static fi.livi.digitraffic.meri.util.TimeUtil.timeToString;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.livi.digitraffic.meri.portnet.vesseldetails.xsd.VesselList;
import fi.livi.digitraffic.meri.service.portnet.PortCallClient;
import fi.livi.digitraffic.meri.util.web.Jax2bRestTemplate;

@Service
@ConditionalOnNotWebApplication
public class VesselDetailsClient {
    private final String vesselDetailsUrl;
    private final Jax2bRestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(VesselDetailsClient.class);

    public VesselDetailsClient(@Value("${dt.portnet.vesselDetails.url}") final String vesselDetailsUrl,
                               final Jax2bRestTemplate restTemplate) {
        this.vesselDetailsUrl = vesselDetailsUrl;
        this.restTemplate = restTemplate;
    }

    public VesselList getVesselList(final ZonedDateTime from) {
        final String url = buildUrl(from);
        final VesselList vesselList = restTemplate.getForObject(url, VesselList.class);

//        logInfo(vesselList);

        return vesselList;
    }

    private static void logInfo(final VesselList vesselList) {
        log.info("Number of received vessel details: " + vesselList.getVesselDetails().size());

        final ObjectMapper mapper = new ObjectMapper();
        try {
            log.info(mapper.writeValueAsString(vesselList));
        } catch (final JsonProcessingException e) {
            log.error("Could not parse", e);
        }
    }

    private String buildUrl(final ZonedDateTime from) {
        final String dateFromParameter = dateToString("fromDte", from);
        final String timeFromParameter = timeToString("fromTme", from);

        return String.format("%s%s&%s", vesselDetailsUrl, dateFromParameter, timeFromParameter);
    }
}
