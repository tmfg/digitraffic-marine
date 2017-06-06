package fi.livi.digitraffic.meri.service.winternavigation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import fi.livi.digitraffic.meri.AbstractIntegrationTest;
import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationShipRepository;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationShip;

public class WinterNavigationShipUpdaterTest extends AbstractIntegrationTest {

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private WinterNavigationClient winterNavigationClient;

    @MockBean(answer =  Answers.CALLS_REAL_METHODS)
    private WinterNavigationShipUpdater winterNavigationShipUpdater;

    @Autowired
    private WinterNavigationShipRepository winterNavigationShipRepository;

    @Autowired
    private UpdatedTimestampRepository updatedTimestampRepository;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer server;

    private final String expectedUri = "winterNavigationUrl";

    @Before
    public void before() {
        winterNavigationClient = new WinterNavigationClient(expectedUri, restTemplate);
        winterNavigationShipUpdater = new WinterNavigationShipUpdater(winterNavigationClient, winterNavigationShipRepository, updatedTimestampRepository);
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @Transactional
    @Rollback
    public void updateWinterNavigationShipsSucceeds() throws IOException {

        expectResponse("winterNavigationShipsResponse1.xml");
        expectResponse("winterNavigationShipsResponse2.xml");

        winterNavigationShipUpdater.updateWinterNavigationShips();

        List<WinterNavigationShip> ships = winterNavigationShipRepository.findDistinctByOrderByVesselPK();
        WinterNavigationShip ship = ships.stream().filter(s -> s.getVesselPK().equals("IMO-9386524")).findFirst().get();

        assertTrue(ships.size() > 0);
        assertEquals("9386524", ship.getImo());
        assertEquals("A La Marine", ship.getName());
        assertEquals("HK", ship.getNatCode());
        assertEquals(171.0, ship.getAisLength(), 0.1);
        assertEquals("under way using engine", ship.getShipState().getAisStateText());
        assertEquals(14.8, ship.getShipState().getSpeed(), 0.1);

        assertEquals(1, ship.getShipActivities().size());
        assertEquals("NOT", ship.getShipActivities().get(0).getActivityType());
        assertEquals(Timestamp.from(ZonedDateTime.parse("2017-04-03T15:14:06.000+00:00").toInstant()), ship.getShipActivities().get(0).getBeginTime());
        assertEquals(0, ship.getShipPlannedActivities().size());

        winterNavigationShipUpdater.updateWinterNavigationShips();

        ships = winterNavigationShipRepository.findDistinctByOrderByVesselPK();
        ship = ships.stream().filter(s -> s.getVesselPK().equals("IMO-9386524")).findFirst().get();

        assertEquals("A La Marine", ship.getName());
        assertEquals(15.8, ship.getShipState().getSpeed(), 0.1);
        assertEquals("under way using engine", ship.getShipState().getAisStateText());

        assertEquals(0, ship.getShipActivities().size());
        assertEquals(2, ship.getShipPlannedActivities().size());
        assertEquals("IMO-8418174", ship.getShipPlannedActivities().get(0).getPlannedVesselPK());
        assertEquals(0, ship.getShipPlannedActivities().get(0).getOrdering().intValue());
        assertEquals("IMO-9167344", ship.getShipPlannedActivities().get(1).getPlannedVesselPK());
        assertEquals("Planned assistance", ship.getShipPlannedActivities().get(1).getActivityText());

        server.verify();
    }

    private void expectResponse(final String filename) throws IOException {
        server.expect(MockRestRequestMatchers.requestTo(expectedUri + "/ships"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withSuccess(readFile(filename), MediaType.APPLICATION_XML));
    }
}
