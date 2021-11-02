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
import org.springframework.util.Assert;

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
        final String portArea,
        final String from,
        final BigInteger portcallId) {
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.recordTime = recordTime;
        this.eventTimeConfidenceLower = null;
        this.eventTimeConfidenceUpper = null;
        this.source = "Portnet";
        this.ship = ship;
        this.location = new Location(portToVisit, portArea, from);
        this.portcallId = portcallId;
    }

}

enum EventType {
    ETA, ETD, ATA
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
    public final String portArea;
    public final String from;

    public Location(
        final String port,
        final String portArea,
        final String from) {
        this.port = port;
        this.portArea = portArea;
        this.from = from;
    }
}

interface PortcallEstimateUpdater {
    void updatePortcallEstimate(final PortCallNotification pcn);
}

class PortcallEstimateEndpoint {

    final String url;
    final String apiKey;

    PortcallEstimateEndpoint(final String url, final String apiKey) {
        this.url = url;
        this.apiKey = apiKey;
    }

}

@ConditionalOnNotWebApplication
@ConditionalOnProperty({"portcallestimate.urls", "portcallestimate.apikeys"})
@Component
class HttpPortcallEstimateUpdater implements PortcallEstimateUpdater {

    private final HttpClient httpClient = HttpClients.createDefault();
    private final List<PortcallEstimateEndpoint> portcallEstimateEndpoints = new ArrayList<>();

    private final ObjectMapper om;

    private static final Logger log = LoggerFactory.getLogger(HttpPortcallEstimateUpdater.class);

    @Autowired
    public HttpPortcallEstimateUpdater(
        @Value("${portcallestimate.urls}") final String portcallEstimateUrls,
        @Value("${portcallestimate.apikeys}") final String portcallEstimateApiKeys,
        final ObjectMapper om
        )
    {
        final String[] urls = portcallEstimateUrls.split(",");
        final String[] apiKeys = portcallEstimateApiKeys.split(",");
        Assert.isTrue(urls.length == apiKeys.length,
            "Portcall estimate endpoint URL amount was not equal to API key amount");
        for (int i = 0; i < urls.length; i++) {
            portcallEstimateEndpoints.add(new PortcallEstimateEndpoint(urls[i], apiKeys[i]));
        }
        this.om = om;
    }

    @Override
    public void updatePortcallEstimate(final PortCallNotification pcn) {
        final List<PortcallEstimate> pces = estimatesFromPortcallNotification(pcn);
        log.info("method=updatePortcallEstimate created {} estimates from portcall notification", pces.size());
        for (final PortcallEstimateEndpoint endpoint : portcallEstimateEndpoints) {
            updatePortcallEstimateToEndpoint(pces, endpoint);
        }
    }

    private void updatePortcallEstimateToEndpoint(
        final List<PortcallEstimate> pces,
        final PortcallEstimateEndpoint endpoint) {

        for (final PortcallEstimate pce : pces) {
            try {
                final HttpPost post = new HttpPost(endpoint.url);
                post.setHeader("X-Api-Key", endpoint.apiKey);
                final String json = om.writeValueAsString(pce);
                post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
                final StatusLine status = httpClient.execute(post).getStatusLine();
                if (status.getStatusCode() != HttpStatus.SC_OK) {
                    log.warn("method=updatePortcallEstimate got status code {}, reason {}", status.getStatusCode(), status.getReasonPhrase());
                } else {
                    log.info("method=updatePortcallEstimate Updated portcall estimate {}", json);
                }
                post.releaseConnection();
            } catch (final Exception e) {
                log.warn("method=updatePortcallEstimate exception", e);
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

        final PortcallEstimate ataEstimate = getAtaEstimate(pcn.getPortCallId(), details, pcn.getPortCallDetails());
        if (ataEstimate != null) {
            estimates.add(ataEstimate);
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
                log.warn("method=getEtaEstimate ship was null for ETA port call {}", portcallId);
                return null;
            }
            return new PortcallEstimate(EventType.ETA,
                ZonedDateTime.ofInstant(bd.getEta().toGregorianCalendar().toInstant(), FINLAND_ZONE),
                ZonedDateTime.ofInstant(bd.getEtaTimeStamp().toGregorianCalendar().toInstant(), FINLAND_ZONE),
                ship,
                portCallDetails.getPortToVisit(),
                portAreaDetails.getPortAreaCode(),
                portCallDetails.getPrevPort(),
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
                log.warn("method=getEtaEstimate ship was null for ETD port call {}", portcallId);
                return null;
            }
            return new PortcallEstimate(EventType.ETD,
                ZonedDateTime.ofInstant(bd.getEtd().toGregorianCalendar().toInstant(), FINLAND_ZONE),
                ZonedDateTime.ofInstant(bd.getEtdTimeStamp().toGregorianCalendar().toInstant(), FINLAND_ZONE),
                ship,
                portCallDetails.getPortToVisit(),
                portAreaDetails.getPortAreaCode(),
                portCallDetails.getPrevPort(),
                portcallId);
        }
        return null;
    }

    private PortcallEstimate getAtaEstimate(
        final BigInteger portcallId,
        final PortAreaDetails portAreaDetails,
        final PortCallDetails portCallDetails) {

        final BerthDetails bd = portAreaDetails.getBerthDetails();
        if (bd.getAta() != null && bd.getAtaTimeStamp() != null) {
            final Ship ship = getShipFromVesselDetails(portCallDetails.getVesselDetails());
            if (ship == null) {
                log.warn("method=getEtaEstimate ship was null for ATA port call {}", portcallId);
                return null;
            }
            return new PortcallEstimate(EventType.ATA,
                ZonedDateTime.ofInstant(bd.getAta().toGregorianCalendar().toInstant(), FINLAND_ZONE),
                ZonedDateTime.ofInstant(bd.getAtaTimeStamp().toGregorianCalendar().toInstant(), FINLAND_ZONE),
                ship,
                portCallDetails.getPortToVisit(),
                portAreaDetails.getPortAreaCode(),
                portCallDetails.getPrevPort(),
                portcallId);
        }
        return null;
    }

    private Ship getShipFromVesselDetails(final VesselDetails vesselDetails) {
        if (vesselDetails.getIdentificationData() == null) {
            log.warn("method=getShipFromVesselDetails vessel identification data was null");
            return null;
        }
        if (vesselDetails.getIdentificationData().getMmsi() == null && vesselDetails.getIdentificationData().getImoLloyds() == null) {
            log.warn("method=getShipFromVesselDetails mmsi & imo were null");
            return null;
        }
        final BigInteger mmsi = vesselDetails.getIdentificationData().getMmsi();
        final BigInteger imo = vesselDetails.getIdentificationData().getImoLloyds();

        return new Ship(
            mmsi != null ? mmsi.intValue() : null,
            imo != null ? imo.intValue() : null);
    }
}
