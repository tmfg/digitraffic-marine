package fi.livi.digitraffic.meri.service.portnet;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.livi.digitraffic.meri.portnet.xsd.*;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public final BigInteger portcallId;

    public PortcallEstimate(
        final EventType eventType,
        final ZonedDateTime eventTime,
        final ZonedDateTime recordTime,
        final Ship ship,
        final String portToVisit,
        final BigInteger portcallId) {
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.recordTime = recordTime;
        this.eventTimeConfidenceLower = null;
        this.eventTimeConfidenceUpper = null;
        this.source = "Portnet";
        this.ship = ship;
        this.location = new Location(portToVisit);
        this.portcallId = portcallId;
    }

}

enum EventType {
    ETA, ETD
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
@ConditionalOnProperty({"portcallestimate.url", "portcallestimate.apikey"})
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
        final List<PortcallEstimate> pces = estimatesFromPortcallNotification(pcn);
        log.info("method=updatePortcallEstimate created {} estimates from portcall notification", pces.size());
        for (PortcallEstimate pce : pces) {
            try {
                final HttpPost post = new HttpPost(portcallEstimateUrl);
                post.setHeader("X-Api-Key", portcallEstimateApiKey);
                final String json = om.writeValueAsString(pce);
                log.info("method=updatePortcallEstimate about to send json {}, original mmsi {}, original imo {}", json, pcn.getPortCallDetails().getVesselDetails().getIdentificationData().getMmsi(), pcn.getPortCallDetails().getVesselDetails().getIdentificationData().getImoLloyds());
                post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
                final StatusLine status = httpClient.execute(post).getStatusLine();
                if (status.getStatusCode() != HttpStatus.SC_OK) {
                    log.warn("method=updatePortcallEstimate got status code {}, reason {}", status.getStatusCode(), status.getReasonPhrase());
                } else {
                    log.info("method=updatePortcallEstimate Updated portcall estimate {}", json);
                }
                post.releaseConnection();
            } catch (Throwable t) {
                log.warn("method=updatePortcallEstimate unexpected error", t);
            }
        }
    }

    private List<PortcallEstimate> estimatesFromPortcallNotification(final PortCallNotification pcn) {
        final List<PortcallEstimate> estimates = new ArrayList<>();
        final PortAreaDetails details = pcn.getPortCallDetails().getPortAreaDetails().get(0); // seems to be always 1

        final PortcallEstimate etaEstimate = getEtaEstimate(pcn.getPortCallId(), details, pcn.getPortCallDetails());
        if (etaEstimate != null) {
            estimates.add(etaEstimate);
        }

        final PortcallEstimate etdEstimate = getEtdEstimate(pcn.getPortCallId(), details, pcn.getPortCallDetails());
        if (etdEstimate != null) {
            estimates.add(etdEstimate);
        }

        return estimates;
    }

    private PortcallEstimate getEtaEstimate(
        final BigInteger portcallId,
        final PortAreaDetails portAreaDetails,
        final PortCallDetails portCallDetails) {

        final BerthDetails bd = portAreaDetails.getBerthDetails();
        if (bd.getEta() != null && bd.getEtaTimeStamp() != null) {
            final Ship ship = getShipFromVesselDetails(portCallDetails.getVesselDetails());
            if (ship == null) {
                return null;
            }
            return new PortcallEstimate(EventType.ETA,
                ZonedDateTime.ofInstant(bd.getEta().toGregorianCalendar().toInstant(), FINLAND_ZONE),
                ZonedDateTime.ofInstant(bd.getEtaTimeStamp().toGregorianCalendar().toInstant(), FINLAND_ZONE),
                ship,
                portCallDetails.getPortToVisit(),
                portcallId);
        }
        return null;
    }

    private PortcallEstimate getEtdEstimate(
        final BigInteger portcallId,
        final PortAreaDetails portAreaDetails,
        final PortCallDetails portCallDetails) {

        final BerthDetails bd = portAreaDetails.getBerthDetails();
        if (bd.getEtd() != null && bd.getEtdTimeStamp() != null) {
            final Ship ship = getShipFromVesselDetails(portCallDetails.getVesselDetails());
            if (ship == null) {
                return null;
            }
            return new PortcallEstimate(EventType.ETD,
                ZonedDateTime.ofInstant(bd.getEtd().toGregorianCalendar().toInstant(), FINLAND_ZONE),
                ZonedDateTime.ofInstant(bd.getEtdTimeStamp().toGregorianCalendar().toInstant(), FINLAND_ZONE),
                ship,
                portCallDetails.getPortToVisit(),
                portcallId);
        }
        return null;
    }

    private Ship getShipFromVesselDetails(final VesselDetails vesselDetails) {
        if (vesselDetails.getIdentificationData() == null) {
            return null;
        }
        if (vesselDetails.getIdentificationData().getMmsi() == null && vesselDetails.getIdentificationData().getImoLloyds() == null) {
            return null;
        }
        final BigInteger mmsi = vesselDetails.getIdentificationData().getMmsi();
        final BigInteger imo = vesselDetails.getIdentificationData().getImoLloyds();

        return new Ship(
            mmsi != null ? mmsi.intValue() : null,
            imo != null ? imo.intValue() : null);
    }
}
