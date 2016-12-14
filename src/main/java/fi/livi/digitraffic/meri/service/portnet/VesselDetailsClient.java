package fi.livi.digitraffic.meri.service.portnet;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.portnet.vesseldetails.xsd.VesselList;

@Service
public class VesselDetailsClient {

    private final String vesselDetailsUrl;

    private static final Logger log = LoggerFactory.getLogger(VesselDetailsClient.class);

    public VesselDetailsClient(@Value("${ais.portnet.vesselDetails.url}") final String vesselDetailsUrl) {
        this.vesselDetailsUrl = vesselDetailsUrl;
    }

    public VesselList getVesselList(final Instant from) throws Exception {
        final String url = buildUrl(from);
        RestTemplate restTemplate = getRestTemplate();

        VesselList vesselList = restTemplate.getForObject(url, VesselList.class);

        logInfo(vesselList);

        return vesselList;
    }

    protected RestTemplate getRestTemplate() {
        return new RestTemplate();
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

    private String buildUrl(final Instant from) {
        final ZonedDateTime fromDt = from.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.systemDefault());
        final String dateFromParameter = PortCallClient.dateToString("fromDte", fromDt);
        final String timeFromParameter = PortCallClient.timeToString("fromTme", fromDt);

        return String.format("%s%s&%s", vesselDetailsUrl, dateFromParameter, timeFromParameter);
    }
}
