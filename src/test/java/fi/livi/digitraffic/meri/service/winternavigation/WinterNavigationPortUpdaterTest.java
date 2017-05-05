package fi.livi.digitraffic.meri.service.winternavigation;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
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
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationRepository;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationPort;

public class WinterNavigationPortUpdaterTest extends AbstractIntegrationTest {

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private WinterNavigationClient winterNavigationClient;

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private WinterNavigationPortUpdater winterNavigationPortUpdater;

    @Autowired
    private WinterNavigationRepository winterNavigationRepository;

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
    }
}
