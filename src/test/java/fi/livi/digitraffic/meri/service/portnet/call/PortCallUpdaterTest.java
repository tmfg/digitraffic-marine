package fi.livi.digitraffic.meri.service.portnet.call;

import fi.livi.digitraffic.meri.AbstractDaemonTestBase;
import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.dto.portcall.v1.call.PortCallJsonV1;
import fi.livi.digitraffic.meri.portnet.xsd.PortCallNotification;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static fi.livi.digitraffic.common.util.TimeUtil.FINLAND_ZONE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PortCallUpdaterTest extends AbstractDaemonTestBase {
    @Autowired
    private UpdatedTimestampRepository updatedTimestampRepository;

    @Autowired
    private PortCallRepository portCallRepository;

    @Autowired
    private WebClient portnetWebClient;

    private PortCallClient portCallClient;
    private PortCallUpdater portCallUpdater;
    private MockWebServer server;

    @BeforeEach
    public void before() {
        server = new MockWebServer();
        portCallClient = Mockito.spy(new PortCallClient(server.url("/portCallUrl/").toString(), portnetWebClient));
        portCallUpdater = new PortCallUpdater(
            portCallRepository,
            updatedTimestampRepository,
            portCallClient,
            Optional.of(new NoOpPortcallEstimateUpdater()),
            42,
            42);
    }

    @AfterEach
    public void after() throws IOException {
        server.close();
    }

    @Test
    @Transactional
    @Rollback
    public void updatePortCallsSucceeds() throws IOException, InterruptedException {
        addXmlResponseFromFile(server, "portcalls/portCallResponse1.xml");
        addXmlResponseFromFile(server, "portcalls/portCallResponse2.xml");

        final ZonedDateTime from = ZonedDateTime.of(2016, 1, 30, 6, 30, 59, 0, FINLAND_ZONE);
        final ZonedDateTime to = ZonedDateTime.of(2016, 1, 30, 6, 36, 30, 0, FINLAND_ZONE);

        portCallUpdater.updatePortCalls(from, to);

        expectResponse(server, "/portCallUrl/startDte=20160130&endDte=20160130&startTme=063059&endTme=063630");

        final List<PortCallJsonV1> portCalls = portCallRepository.findByPortCallIdIn(Arrays.asList(1975743L, 1975010L)).stream()
            .sorted(Comparator.comparing(PortCallJsonV1::getPortCallId))
            .collect(Collectors.toList());

        assertTrue(portCalls.size() >= 2);

        final PortCallJsonV1 portCall1 = portCalls.stream().filter(portCall ->
            portCall.getPortCallId().equals(1975743L) &&
                portCall.getPortCallTimestamp().equals(new Timestamp(1479904755000L))).collect(Collectors.toList()).get(0);
        assertEquals("FIVAA", portCall1.getPortToVisit());
        assertEquals("Wasa Express", portCall1.getVesselName());

        final PortCallJsonV1 portCall2 = portCalls.stream().filter(portCall ->
            portCall.getPortCallId().equals(1975010L) &&
                portCall.getPortCallTimestamp().equals(new Timestamp(1479904750000L))).collect(Collectors.toList()).get(0);
        assertEquals("FIRAU", portCall2.getPortToVisit());
        assertEquals("Mons", portCall2.getVesselName());

        portCallUpdater.updatePortCalls(from, to);

        expectResponse(server, "/portCallUrl/startDte=20160130&endDte=20160130&startTme=063059&endTme=063630");

        final PortCallJsonV1 updatedPortCall = portCallRepository.findByPortCallIdIn(Arrays.asList(1975743L)).get(0);

        assertEquals("FOO", updatedPortCall.getPortToVisit());
        assertEquals("Vessel name", updatedPortCall.getVesselName());
    }

    @Test
    @Transactional
    @Rollback
    public void timestampCheck() throws IOException, InterruptedException {
        addXmlResponseFromFile(server, "portcalls/portCallResponse_timestamp.xml");
        addXmlResponseFromFile(server, "portcalls/portCallResponse_timestamp.xml");

        final ZonedDateTime from = ZonedDateTime.of(2016, 1, 30, 6, 30, 59, 0, FINLAND_ZONE);
        final ZonedDateTime to = ZonedDateTime.of(2016, 1, 30, 6, 36, 30, 0, FINLAND_ZONE);

        portCallUpdater.updatePortCalls(from, to);

        expectResponse(server, "/portCallUrl/startDte=20160130&endDte=20160130&startTme=063059&endTme=063630");
        expectResponse(server, "/portCallUrl/startDte=20160130&endDte=20160130&startTme=063059&endTme=063630");

        verify(portCallClient, times(2)).getList(any(), any());
    }

    private static class NoOpPortcallEstimateUpdater implements PortcallEstimateUpdater {
        @Override
        public void updatePortcallEstimate(final PortCallNotification pcn) {}
    }

}
