package fi.livi.digitraffic.meri.service.portnet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.portnet.xsd.*;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.math.BigInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Rollback
public class PortcallEstimateUpdaterTest extends AbstractTestBase {
    private static final String TEST_API_KEY = "test_value";

    private static final String ETA_TIME = "2021-10-10T10:45:00+03:00";
    private static final String ETD_TIME = "2021-10-10T11:45:00+03:00";
    private static final String ATA_TIME = "2021-10-10T12:45:00+03:00";

    private static final Integer PORTCALL_ID = 123;
    private static final String PORT_TO_VISIT = "FITST";

    @Autowired
    private ObjectMapper objectMapper;

    private WireMockServer mockServer() {
        final WireMockServer server = new WireMockServer();

        server.start();

        server.givenThat(
            get(urlEqualTo(""))
                .willReturn(aResponse()
                    .withStatus(200)
                )
        );

        return server;
    }

    private PortCallNotification createNotification() throws DatatypeConfigurationException {
        final XMLGregorianCalendar etaCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(ETA_TIME);
        final XMLGregorianCalendar etdCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(ETD_TIME);
        final XMLGregorianCalendar ataCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(ATA_TIME);

        final PortCallNotification pcn = new PortCallNotification();
        final PortCallDetails pcd = new PortCallDetails();
        final PortAreaDetails pad = new PortAreaDetails();
        final BerthDetails bd = new BerthDetails();

        final VesselDetails vd = new VesselDetails();
        final VesselDetails.IdentificationData id = new VesselDetails.IdentificationData();

        pcn.setPortCallDetails(pcd);
        pcn.setPortCallId(BigInteger.valueOf(PORTCALL_ID));

        pcd.getPortAreaDetails().add(pad);
        pcd.setVesselDetails(vd);
        pcd.setPortToVisit(PORT_TO_VISIT);

        vd.setIdentificationData(id);
        id.setMmsi(BigInteger.ONE);
        id.setImoLloyds(BigInteger.ONE);

        pad.setBerthDetails(bd);

        bd.setEta(etaCal);
        bd.setEtaTimeStamp(etaCal);

        bd.setEtd(etdCal);
        bd.setEtdTimeStamp(etdCal);

        bd.setAta(ataCal);
        bd.setAtaTimeStamp(ataCal);

        return pcn;
    }

    private RequestPatternBuilder createPattern() {
        return postRequestedFor(urlEqualTo("/"))
            .withHeader("X-Api-Key", equalTo(TEST_API_KEY))
            .withHeader("Content-Type", equalTo(ContentType.APPLICATION_JSON.toString()))
            .withRequestBody(matchingJsonPath("$.source", equalTo("Portnet")))
            .withRequestBody(matchingJsonPath("$.ship.mmsi", equalTo("1")))
            .withRequestBody(matchingJsonPath("$.ship.imo", equalTo("1")))
            .withRequestBody(matchingJsonPath("$.portcallId", equalTo(PORTCALL_ID.toString())))
            .withRequestBody(matchingJsonPath("$.location.port", equalTo(PORT_TO_VISIT)))
        ;
    }

    @Test
    public void updatePortcallEstimate() throws DatatypeConfigurationException {
        final WireMockServer server = mockServer();

        try {
            final PortcallEstimateUpdater updater = new HttpPortcallEstimateUpdater(server.baseUrl(),
                TEST_API_KEY, objectMapper);

            updater.updatePortcallEstimate(createNotification());

            // verify that all 3 timestamps are included
            server.verify(createPattern()
                .withRequestBody(matchingJsonPath("$.eventType", equalTo("ATA")))
                .withRequestBody(matchingJsonPath("$.eventTime", equalTo(ATA_TIME)))
            );

            server.verify(createPattern()
                .withRequestBody(matchingJsonPath("$.eventType", equalTo("ETA")))
                .withRequestBody(matchingJsonPath("$.eventTime", equalTo(ETA_TIME)))
            );

            server.verify(createPattern()
                .withRequestBody(matchingJsonPath("$.eventType", equalTo("ETD")))
                .withRequestBody(matchingJsonPath("$.eventTime", equalTo(ETD_TIME)))
            );
        } finally {
            server.stop();
        }
    }
}
