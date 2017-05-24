package fi.livi.digitraffic.meri.service.winternavigation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Timestamp;
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
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationPortRepository;
import fi.livi.digitraffic.meri.domain.winternavigation.PortRestriction;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationPort;

public class WinterNavigationPortUpdaterTest extends AbstractIntegrationTest {

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private WinterNavigationClient winterNavigationClient;

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private WinterNavigationPortUpdater winterNavigationPortUpdater;

    @Autowired
    private WinterNavigationPortRepository winterNavigationRepository;

    @Autowired
    private UpdatedTimestampRepository updatedTimestampRepository;

    private MockRestServiceServer server;

    private final RestTemplate restTemplate = new RestTemplate();

    private final String expectedUri = "winterNavigationUrl";

    @Before
    public void before() {
        winterNavigationClient = new WinterNavigationClient(expectedUri, restTemplate);
        winterNavigationPortUpdater = new WinterNavigationPortUpdater(winterNavigationClient, winterNavigationRepository, updatedTimestampRepository);
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @Transactional
    @Rollback
    public void updateWinterNavigationPortsSucceeds() throws IOException {

        server.expect(MockRestRequestMatchers.requestTo(expectedUri))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withSuccess(readFile("winterNavigationPortsResponse.xml"), MediaType.APPLICATION_XML));

        winterNavigationPortUpdater.updateWinterNavigationPorts();

        List<WinterNavigationPort> ports = winterNavigationRepository.findAll();
        assertEquals(156, ports.size());
        final WinterNavigationPort port = ports.get(90);
        assertEquals("SEGÄV", port.getLocode());
        assertEquals("GÄVLE", port.getName());
        assertEquals("SE", port.getNationality());
        assertEquals(Double.valueOf("17.18333333"), port.getLongitude(), 0.00000001);
        assertEquals(Double.valueOf("60.68333333"), port.getLatitude(), 0.00000001);
        assertEquals("Sea of Åland and its archipelago", port.getSeaArea());
        assertEquals(1, port.getPortRestrictions().size());

        final PortRestriction restriction = port.getPortRestrictions().get(0);
        assertEquals(1, restriction.getPortRestrictionPK().getOrderNumber().intValue());
        assertTrue(restriction.getCurrent());
        assertFalse(restriction.getPortRestricted());
        assertNull(restriction.getPortClosed());
        assertEquals(Timestamp.valueOf("2017-03-20 08:10:39.127"), restriction.getIssueTime());
        assertEquals(Timestamp.valueOf("2017-03-20 08:10:39.127"), restriction.getLastModified());
        assertNull(restriction.getValidFrom());
        assertNull(restriction.getValidUntil());
        assertNull(restriction.getRawText());
        assertNull(restriction.getFormattedText());
    }
}
