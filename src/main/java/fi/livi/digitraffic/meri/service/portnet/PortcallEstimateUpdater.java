package fi.livi.digitraffic.meri.service.portnet;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.livi.digitraffic.meri.portnet.xsd.BerthDetails;
import fi.livi.digitraffic.meri.portnet.xsd.PortAreaDetails;
import fi.livi.digitraffic.meri.portnet.xsd.PortCallNotification;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.stereotype.Component;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;

import static fi.livi.digitraffic.meri.util.TimeUtil.FINLAND_ZONE;

class PortcallEstimate {

    public final EventType eventType;
    public final ZonedDateTime eventTime;
    public final ZonedDateTime recordTime;
    public final Duration eventTimeConfidenceLower;
    public final Duration eventTimeConfidenceUpper;
    public final String source;
    public final Ship ship;
    public final Location location;

    private PortcallEstimate(final PortCallNotification pcn) {
        PortAreaDetails details = pcn.getPortCallDetails().getPortAreaDetails().get(0); // seems to be always 1
        this.eventType = setEventType(details.getBerthDetails());
        this.eventTime = setEventTime(details.getBerthDetails());//ZonedDateTime.ofInstant(details.getBerthDetails().getEta().toGregorianCalendar().getTime().toInstant(), FINLAND_ZONE);
        this.recordTime = setRecordTime(details.getBerthDetails());//ZonedDateTime.ofInstant(details.getBerthDetails().getEtaTimeStamp().toGregorianCalendar().getTime().toInstant(), FINLAND_ZONE);
        this.eventTimeConfidenceLower = null;
        this.eventTimeConfidenceUpper = null;
        this.source = "DT-" + details.getBerthDetails().getEtaSource().name();
        final BigInteger mmsi = pcn.getPortCallDetails().getVesselDetails().getIdentificationData().getMmsi();
        final BigInteger imo = pcn.getPortCallDetails().getVesselDetails().getIdentificationData().getImoLloyds();
        this.ship = new Ship(mmsi != null ? mmsi.intValue() : null,
            imo != null ? imo.intValue() : null);
        this.location = new Location(pcn.getPortCallDetails().getNextPort());
    }

    public boolean isValid() {
        return eventType != null && eventTime != null && recordTime != null;
    }

    public static PortcallEstimate fromPortcallNotification(final PortCallNotification pcn) {
        return new PortcallEstimate(pcn);
    }

    private EventType setEventType(BerthDetails bd) {
        if (isValidDate(bd.getEta())) {
            return EventType.ETA;
        } else if (isValidDate(bd.getAtd())) {
            return EventType.ATD;
        }
        return null;
    }

    private ZonedDateTime setEventTime(BerthDetails bd) {
        if (isValidDate(bd.getEta())) {
            return ZonedDateTime.ofInstant(bd.getEta().toGregorianCalendar().toInstant(), FINLAND_ZONE);
        } else if (isValidDate(bd.getAtd())) {
            return ZonedDateTime.ofInstant(bd.getAtd().toGregorianCalendar().toInstant(), FINLAND_ZONE);
        }
        return null;
    }

    private ZonedDateTime setRecordTime(BerthDetails bd) {
        if (isValidDate(bd.getEtaTimeStamp())) {
            return ZonedDateTime.ofInstant(bd.getEtaTimeStamp().toGregorianCalendar().toInstant(), FINLAND_ZONE);
        } else if (isValidDate(bd.getAtdTimeStamp())) {
            return ZonedDateTime.ofInstant(bd.getAtdTimeStamp().toGregorianCalendar().toInstant(), FINLAND_ZONE);
        }
        return null;
    }

    private boolean isValidDate(XMLGregorianCalendar cal) {
         return cal != null && cal.toGregorianCalendar().toInstant().isAfter(Instant.now());
    }

    @Override
    public String toString() {
        return "PortcallEstimate{" +
            "eventType=" + eventType +
            ", eventTime=" + eventTime +
            ", recordTime=" + recordTime +
            ", eventTimeConfidenceLower=" + eventTimeConfidenceLower +
            ", eventTimeConfidenceUpper=" + eventTimeConfidenceUpper +
            ", source='" + source + '\'' +
            ", ship=" + ship +
            ", location=" + location +
            '}';
    }
}

enum EventType {
    ETA, ATD, ETD
}

class Ship {
    public final Integer mmsi;
    public final Integer imo;

    public Ship(final Integer mmsi, final Integer imo) {
        this.mmsi = mmsi;
        this.imo = imo;
    }
}

class Location {
    public final String port;

    public Location(final String port) {
        this.port = port;
    }
}

interface PortcallEstimateUpdater {
    void updatePortcallEstimate(PortCallNotification pcn);
}

@ConditionalOnNotWebApplication
@ConditionalOnExpression("'${portcallestimate.url}' == 'null'")
@Component
class NoOpPortcallEstimateUpdater implements PortcallEstimateUpdater {

    @Override
    public void updatePortcallEstimate(PortCallNotification pcn) {}
}

@ConditionalOnNotWebApplication
@ConditionalOnExpression("'${portcallestimate.url}' != 'null'")
@Component
class HttpPortcallEstimateUpdater implements PortcallEstimateUpdater {

    private final HttpClient httpClient = HttpClients.createDefault();
    private final String portcallEstimateUrl;
    private final String portcallEstimateApiKey;
    private final ObjectMapper om;

    private static final Logger log = LoggerFactory.getLogger(HttpPortcallEstimateUpdater.class);

    @Autowired
    public HttpPortcallEstimateUpdater(
        @Value("${portcallestimate.url}") final String portcallEstimateUrl,
        @Value("${portcallestimate.apikey}") final String portcallEstimateApiKey,
        final ObjectMapper om
        )
    {
        this.portcallEstimateUrl = portcallEstimateUrl;
        this.portcallEstimateApiKey = portcallEstimateApiKey;
        this.om = om;
    }

    @Override
    public void updatePortcallEstimate(final PortCallNotification pcn) {
        final PortcallEstimate pce = PortcallEstimate.fromPortcallNotification(pcn);
        try {
            if (!pce.isValid()) {
                throw new IllegalArgumentException("Invalid portcall estimate: " + pce);
            }
            final HttpPost post = new HttpPost(portcallEstimateUrl);
            post.setHeader("X-Api-Key", portcallEstimateApiKey);
            final String json = om.writeValueAsString(pce);
            post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            final StatusLine status = httpClient.execute(post).getStatusLine();
            if (status.getStatusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException("Failed to update portcall estimate: " + status.getStatusCode() + ", " + status.getReasonPhrase() + ", " + json);
            }
            log.info("method=updatePortcallEstimate Updated portcall estimate {}", json);
        } catch (Exception e) {
            log.warn("method=updatePortcallEstimate unable to update portcall estimates", e);
        }
    }
}
