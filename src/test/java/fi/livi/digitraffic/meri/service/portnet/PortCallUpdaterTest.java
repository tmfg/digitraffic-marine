package fi.livi.digitraffic.meri.service.portnet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
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
import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallJson;

public class PortCallUpdaterTest extends AbstractIntegrationTest {

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private PortCallClient portCallClient;

    @Autowired
    private PortCallUpdater portCallUpdater;

    @Autowired
    private PortCallRepository portCallRepository;

    private MockRestServiceServer server;

    private RestTemplate restTemplate = new RestTemplate();

    @Before
    public void before() {
        server = MockRestServiceServer.createServer(restTemplate);
        Mockito.when(portCallClient.getRestTemplate()).thenReturn(restTemplate);
    }

    @Test
    @Transactional
    @Rollback
    public void updatePortCallsSucceeds() throws FileNotFoundException {

        String response = readFile("portCallResponse1.xml");

        server.expect(MockRestRequestMatchers.requestTo("nullstartDte=20160130&endDte=20160130&startTme=063059&endTme=063630"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andExpect(MockRestRequestMatchers.header("Accept", "application/xml, text/xml, application/json, application/*+xml, application/*+json"))
                .andRespond(MockRestResponseCreators.withSuccess(response, MediaType.APPLICATION_XML));

        response = readFile("portCallResponse2.xml");

        server.expect(MockRestRequestMatchers.requestTo("nullstartDte=20160130&endDte=20160130&startTme=063059&endTme=063630"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess(response, MediaType.APPLICATION_XML));

        Instant from = ZonedDateTime.of(2016, 1, 30, 6, 30, 59, 0, ZoneId.systemDefault()).toInstant();
        Instant to = ZonedDateTime.of(2016, 1, 30, 6, 36, 30, 0, ZoneId.systemDefault()).toInstant();

        portCallUpdater.updatePortCalls(from, to);

        List<PortCallJson> portCalls = portCallRepository.findByPortCallIdIn(Arrays.asList(1975743L, 1975010L)).stream()
                .sorted(Comparator.comparing(PortCallJson::getPortCallId))
                .collect(Collectors.toList());

        assertTrue(portCalls.size() >= 2);

        PortCallJson portCall1 = portCalls.stream().filter(portCall ->
                portCall.getPortCallId().equals(1975743L) &&
                portCall.getPortCallTimestamp().equals(new Timestamp(1479904755000L))).collect(Collectors.toList()).get(0);
        assertEquals("FIVAA", portCall1.getPortToVisit());
        assertEquals("Wasa Express", portCall1.getVesselName());

        PortCallJson portCall2 = portCalls.stream().filter(portCall ->
                portCall.getPortCallId().equals(1975010L) &&
                portCall.getPortCallTimestamp().equals(new Timestamp(1479904750000L))).collect(Collectors.toList()).get(0);
        assertEquals("FIRAU", portCall2.getPortToVisit());
        assertEquals("Mons", portCall2.getVesselName());

        portCallUpdater.updatePortCalls(from, to);
        server.verify();

        PortCallJson updatedPortCall = portCallRepository.findByPortCallIdIn(Arrays.asList(1975743L)).get(0);

        assertEquals("FOO", updatedPortCall.getPortToVisit());
        assertEquals("Vessel name", updatedPortCall.getVesselName());
    }
}
