package fi.livi.digitraffic.meri.service.portnet;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.domain.portnet.PortAreaDetails;
import fi.livi.digitraffic.meri.domain.portnet.PortCall;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallsJson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Transactional
public class PortCallServiceTest extends AbstractTestBase {

    @Autowired
    private PortCallService portCallService;

    @Autowired
    private PortCallRepository portCallRepository;

    @AfterEach
    public void after() {
        portCallRepository.deleteAll();
    }

    @Test
    public void etaBetween() {
        newPortCall(Timestamp.from(Instant.now()), null, null, null);

        final PortCallsJson pcj = portCallService.findPortCalls(null,
            null,
            null,
            ZonedDateTime.now().minus(3, ChronoUnit.DAYS),
            ZonedDateTime.now().plus(3, ChronoUnit.DAYS),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(1, pcj.portCalls.size());
    }

    @Test
    public void etaAfter_openEnd() {
        newPortCall(Timestamp.from(Instant.now()), null, null, null);

        final PortCallsJson pcj = portCallService.findPortCalls(null,
            null,
            null,
            ZonedDateTime.now().minus(3, ChronoUnit.DAYS),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(1, pcj.portCalls.size());
    }

    @Test
    public void etaBefore_openStart() {
        newPortCall(Timestamp.from(Instant.now()), null, null, null);

        final PortCallsJson pcj = portCallService.findPortCalls(null,
            null,
            null,
            null,
            ZonedDateTime.now().plus(3, ChronoUnit.DAYS),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(1, pcj.portCalls.size());
    }

    @Test
    public void etaOutside() {
        newPortCall(Timestamp.from(Instant.now().minus(10, ChronoUnit.DAYS)), null, null, null);

        final PortCallsJson pcj = portCallService.findPortCalls(null,
            null,
            null,
            ZonedDateTime.now().minus(3, ChronoUnit.DAYS),
            ZonedDateTime.now().plus(3, ChronoUnit.DAYS),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(0, pcj.portCalls.size());
    }

    @Test
    public void etdBetween() {
        newPortCall(null, Timestamp.from(Instant.now()), null, null);

        final PortCallsJson pcj = portCallService.findPortCalls(null,
            null,
            null,
            null,
            null,
            ZonedDateTime.now().minus(3, ChronoUnit.DAYS),
            ZonedDateTime.now().plus(3, ChronoUnit.DAYS),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(1, pcj.portCalls.size());
    }

    @Test
    public void etdAfter_openEnd() {
        newPortCall(null, Timestamp.from(Instant.now()), null, null);

        final PortCallsJson pcj = portCallService.findPortCalls(null,
            null,
            null,
            null,
            null,
            ZonedDateTime.now().minus(3, ChronoUnit.DAYS),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(1, pcj.portCalls.size());
    }

    @Test
    public void etdBefore_openStart() {
        newPortCall(null, Timestamp.from(Instant.now()), null, null);

        final PortCallsJson pcj = portCallService.findPortCalls(null,
            null,
            null,
            null,
            null,
            null,
            ZonedDateTime.now().plus(3, ChronoUnit.DAYS),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(1, pcj.portCalls.size());
    }

    @Test
    public void etdOutside() {
        newPortCall(null, Timestamp.from(Instant.now().minus(10, ChronoUnit.DAYS)), null, null);

        final PortCallsJson pcj = portCallService.findPortCalls(null,
            null,
            null,
            null,
            null,
            ZonedDateTime.now().minus(3, ChronoUnit.DAYS),
            ZonedDateTime.now().plus(3, ChronoUnit.DAYS),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(0, pcj.portCalls.size());
    }

    @Test
    public void ataBetween() {
        newPortCall(null, null, Timestamp.from(Instant.now()), null);

        final PortCallsJson pcj = portCallService.findPortCalls(null,
            null,
            null,
            null,
            null,
            null,
            null,
            ZonedDateTime.now().minus(3, ChronoUnit.DAYS),
            ZonedDateTime.now().plus(3, ChronoUnit.DAYS),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(1, pcj.portCalls.size());
    }

    @Test
    public void ataAfter_openEnd() {
        newPortCall(null, null, Timestamp.from(Instant.now()), null);

        final PortCallsJson pcj = portCallService.findPortCalls(null,
            null,
            null,
            null,
            null,
            null,
            null,
            ZonedDateTime.now().minus(3, ChronoUnit.DAYS),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(1, pcj.portCalls.size());
    }

    @Test
    public void ataBefore_openStart() {
        newPortCall(null, null, Timestamp.from(Instant.now()), null);

        final PortCallsJson pcj = portCallService.findPortCalls(null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            ZonedDateTime.now().plus(3, ChronoUnit.DAYS),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(1, pcj.portCalls.size());
    }

    @Test
    public void ataOutside() {
        newPortCall(null, null, Timestamp.from(Instant.now().minus(10, ChronoUnit.DAYS)), null);

        final PortCallsJson pcj = portCallService.findPortCalls(null,
            null,
            null,
            null,
            null,
            null,
            null,
            ZonedDateTime.now().minus(3, ChronoUnit.DAYS),
            ZonedDateTime.now().plus(3, ChronoUnit.DAYS),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(0, pcj.portCalls.size());
    }

    @Test
    public void atdBetween() {
        newPortCall(null, null, null, Timestamp.from(Instant.now()));

        final PortCallsJson pcj = portCallService.findPortCalls(null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            ZonedDateTime.now().minus(3, ChronoUnit.DAYS),
            ZonedDateTime.now().plus(3, ChronoUnit.DAYS),
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(1, pcj.portCalls.size());
    }

    @Test
    public void atdAfter_openEnd() {
        newPortCall(null, null, null, Timestamp.from(Instant.now()));

        final PortCallsJson pcj = portCallService.findPortCalls(null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            ZonedDateTime.now().minus(3, ChronoUnit.DAYS),
            null,
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(1, pcj.portCalls.size());
    }

    @Test
    public void atdBefore_openStart() {
        newPortCall(null, null, null, Timestamp.from(Instant.now()));

        final PortCallsJson pcj = portCallService.findPortCalls(null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            ZonedDateTime.now().plus(3, ChronoUnit.DAYS),
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(1, pcj.portCalls.size());
    }

    @Test
    public void atdOutside() {
        newPortCall(null, null, null, Timestamp.from(Instant.now().minus(10, ChronoUnit.DAYS)));

        final PortCallsJson pcj = portCallService.findPortCalls(null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            ZonedDateTime.now().minus(3, ChronoUnit.DAYS),
            ZonedDateTime.now().plus(3, ChronoUnit.DAYS),
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(0, pcj.portCalls.size());
    }

    private void newPortCall(
        final Timestamp eta,
        final Timestamp etd,
        final Timestamp ata,
        final Timestamp atd
    ) {
        final PortAreaDetails pad = new PortAreaDetails();
        pad.setPortAreaDetailsId(1L);

        if (eta != null) {
            pad.setEta(eta);
        }
        if (etd != null) {
            pad.setEtd(etd);
        }
        if (ata != null) {
            pad.setAta(ata);
        }
        if (atd != null) {
            pad.setAtd(atd);
        }

        final Set<PortAreaDetails> pads = new HashSet<>();
        pads.add(pad);

        final PortCall pc = new PortCall();
        pc.setPortAreaDetails(pads);
        pc.setPortCallId(1L);
        pc.setRadioCallSign("a");
        pc.setRadioCallSignType("fake");
        pc.setVesselName("test");
        pc.setPortCallTimestamp(Timestamp.from(Instant.now()));
        pc.setPortToVisit("test");
        pc.setMmsi(1);
        pc.setImoLloyds(1);

        portCallRepository.save(pc);
    }
}
