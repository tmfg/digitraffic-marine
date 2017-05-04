package fi.livi.digitraffic.meri.service.winternavigation;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
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

public class WinterNavigationClientTest extends AbstractIntegrationTest {

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private WinterNavigationClient winterNavigationClient;

    private MockRestServiceServer server;

    private final RestTemplate restTemplate = new RestTemplate();

    private final String expectedUri = "winterNavigationUrl";

    @Before
    public void before() {
        winterNavigationClient = new WinterNavigationClient(expectedUri, restTemplate);
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @Transactional
    @Rollback
    public void getWinterNavigationPortsSucceeds() throws IOException {

        server.expect(MockRestRequestMatchers.requestTo(expectedUri))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withSuccess(readFile("winterNavigationPortsResponse.xml"), MediaType.APPLICATION_XML));

        final WinterNavigationPortsDto ports = winterNavigationClient.getWinterNavigationPorts();

        assertEquals(156, ports.ports.size());
        assertEquals("SEGÄV", ports.ports.get(90).portInfo.locode);
        assertEquals(ZonedDateTime.parse("2017-03-20T06:10:39.127Z[UTC]"), ports.ports.get(90).restrictions.get(0).timeStamp);
    }

}