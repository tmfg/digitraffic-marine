package fi.livi.digitraffic.meri.service.winternavigation;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import fi.livi.digitraffic.meri.AbstractIntegrationTest;
import fi.livi.digitraffic.meri.service.winternavigation.dto.PortsDto;
import fi.livi.digitraffic.meri.service.winternavigation.dto.ShipDto;
import fi.livi.digitraffic.meri.service.winternavigation.dto.ShipsDto;

public class WinterNavigationClientTest extends AbstractIntegrationTest {

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private WinterNavigationClient winterNavigationClient;

    @Autowired
    private Jaxb2Marshaller jaxb2Marshaller;

    private MockRestServiceServer server;

    private final RestTemplate restTemplate = new RestTemplate();

    private final String expectedUri = "winterNavigationUrl";

    @Before
    public void before() {
        winterNavigationClient = new WinterNavigationClient(expectedUri, jaxb2Marshaller);
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @Ignore("FIXME")
    public void getWinterNavigationPortsSucceeds() throws IOException {

        server.expect(MockRestRequestMatchers.requestTo(expectedUri))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withSuccess(readFile("winterNavigationPortsResponse.xml"), MediaType.APPLICATION_XML));

        final PortsDto ports = null; //winterNavigationClient.getWinterNavigationPorts();

        assertEquals(156, ports.ports.size());
        assertEquals("SEGÄV", ports.ports.get(90).portInfo.locode);
        assertEquals(ZonedDateTime.parse("2017-03-20T06:10:39.127Z[UTC]"), ports.ports.get(90).restrictions.get(0).timeStamp);
    }

    @Test
    @Ignore("FIXME")
    public void getWinterNavigationShipsSucceeds() throws IOException {

        server.expect(MockRestRequestMatchers.requestTo(expectedUri + "/ships"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withSuccess(readFile("winterNavigationShipsResponse.xml"), MediaType.APPLICATION_XML));

        final ShipsDto ships = winterNavigationClient.getWinterNavigationShips();

        assertEquals(1599, ships.ships.size());
        final ShipDto ship = ships.ships.parallelStream().filter(s -> s.shipData.mmsi.equals("230645000")).findFirst().get();
        assertEquals("IMO-9319064", ship.vesselPk);
        assertEquals("Kallio", ship.shipData.name);
        assertEquals("Finland", ship.shipData.nationality);
        assertEquals("GC", ship.shipData.shipType);
        assertEquals(ZonedDateTime.parse("2017-04-11T10:48:03Z[UTC]"), ship.shipState.timestamp);
        assertEquals(23.24446666, ship.shipState.longitude, 0.00001);
        assertEquals(64.50175, ship.shipState.latitude, 0.00001);
        assertEquals("RAAHE", ship.shipState.aisDestination);
        assertEquals("Moving freely", ship.shipActivities.get(0).activityText);
        assertEquals("IMO-7359656", ship.plannedActivities.get(0).planningVesselPK);
    }
}