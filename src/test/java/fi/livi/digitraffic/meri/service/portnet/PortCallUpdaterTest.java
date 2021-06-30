package fi.livi.digitraffic.meri.service.portnet;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallJson;
import fi.livi.digitraffic.meri.portnet.xsd.PortCallNotification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static fi.livi.digitraffic.meri.util.TimeUtil.FINLAND_ZONE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PortCallUpdaterTest extends AbstractTestBase {
    @Autowired
    private UpdatedTimestampRepository updatedTimestampRepository;

    @Autowired
    private PortCallRepository portCallRepository;

    @Autowired
    private RestTemplate jax2bRestTemplate;

    private PortCallClient portCallClient;
    private PortCallUpdater portCallUpdater;
    private MockRestServiceServer server;

    @BeforeEach
    public void before() {
        portCallClient = Mockito.spy(new PortCallClient("portCallUrl/", jax2bRestTemplate));
        portCallUpdater = new PortCallUpdater(
            portCallRepository,
            updatedTimestampRepository,
            portCallClient,
            Optional.of(new NoOpPortcallEstimateUpdater()),
            42,
            42);
        server = MockRestServiceServer.createServer(jax2bRestTemplate);
    }

    @Test
    @Transactional
    @Rollback
    public void updatePortCallsSucceeds() throws IOException {
        String response = readFile("portcalls/portCallResponse1.xml");

        server.expect(MockRestRequestMatchers.requestTo("/portCallUrl/startDte=20160130&endDte=20160130&startTme=063059&endTme=063630"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withSuccess(response, MediaType.APPLICATION_XML));

        response = readFile("portcalls/portCallResponse2.xml");

        server.expect(MockRestRequestMatchers.requestTo("/portCallUrl/startDte=20160130&endDte=20160130&startTme=063059&endTme=063630"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withSuccess(response, MediaType.APPLICATION_XML));

        final ZonedDateTime from = ZonedDateTime.of(2016, 1, 30, 6, 30, 59, 0, FINLAND_ZONE);
        final ZonedDateTime to = ZonedDateTime.of(2016, 1, 30, 6, 36, 30, 0, FINLAND_ZONE);

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

    @Test
    @Transactional
    @Rollback
    public void timestampCheck() throws IOException {
        String response = readFile("portcalls/portCallResponse_timestamp.xml");

        server.expect(MockRestRequestMatchers.requestTo("/portCallUrl/startDte=20160130&endDte=20160130&startTme=063059&endTme=063630"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withSuccess(response, MediaType.APPLICATION_XML));

        server.expect(MockRestRequestMatchers.requestTo("/portCallUrl/startDte=20160130&endDte=20160130&startTme=063059&endTme=063630"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withSuccess(response, MediaType.APPLICATION_XML));

        final ZonedDateTime from = ZonedDateTime.of(2016, 1, 30, 6, 30, 59, 0, FINLAND_ZONE);
        final ZonedDateTime to = ZonedDateTime.of(2016, 1, 30, 6, 36, 30, 0, FINLAND_ZONE);

        portCallUpdater.updatePortCalls(from, to);

        verify(portCallClient, times(2)).getList(any(), any());
    }

    private static class NoOpPortcallEstimateUpdater implements PortcallEstimateUpdater {
        @Override
        public void updatePortcallEstimate(PortCallNotification pcn) {}
    }

}
