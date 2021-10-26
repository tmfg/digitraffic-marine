package fi.livi.digitraffic.meri.service.portnet;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.domain.portnet.PortAreaDetails;
import fi.livi.digitraffic.meri.domain.portnet.PortCall;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallsJson;
import fi.livi.digitraffic.meri.service.BadRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.IntStream;

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

    private static final String PORT_LOCODE = "TEST";
    private static final String VESSEL_NAME = "TESTNAME";
    private static final String VESSEL_NATIONALITY = "DT";
    private static final String VESSEL_MASTER_NAME = "MASTER";
    private static final int VESSEL_MMSI = 12345;
    private static final int VESSEL_IMO = 2345;
    private static final int VESSEL_TYPECODE = 5;

    @Test
    public void etaBetween() {
        newPortCall(Timestamp.from(Instant.now()), null, null, null);

        new PortcallQueryBuilder()
            .etaFrom(ZonedDateTime.now().minus(3, ChronoUnit.DAYS))
            .etaTo(ZonedDateTime.now().plus(3, ChronoUnit.DAYS))
            .assertCount(portCallService, 1);
    }

    @Test
    public void etaAfter_openEnd() {
        newPortCall(Timestamp.from(Instant.now()), null, null, null);

        new PortcallQueryBuilder()
            .etaFrom(ZonedDateTime.now().minus(3, ChronoUnit.DAYS))
            .assertCount(portCallService, 1);
    }

    @Test
    public void etaBefore_openStart() {
        newPortCall(Timestamp.from(Instant.now()), null, null, null);

        new PortcallQueryBuilder()
            .etaTo(ZonedDateTime.now().plus(3, ChronoUnit.DAYS))
            .assertCount(portCallService, 1);
    }

    @Test
    public void etaOutside() {
        newPortCall(Timestamp.from(Instant.now()), null, null, null);

        new PortcallQueryBuilder()
            .etaFrom(ZonedDateTime.now().minus(3, ChronoUnit.DAYS))
            .etaTo(ZonedDateTime.now().minus(2, ChronoUnit.DAYS))
            .assertCount(portCallService, 0);
    }

    @Test
    public void etdBetween() {
        newPortCall(null, Timestamp.from(Instant.now()), null, null);

        new PortcallQueryBuilder()
            .etdFrom(ZonedDateTime.now().minus(3, ChronoUnit.DAYS))
            .etdTo(ZonedDateTime.now().plus(3, ChronoUnit.DAYS))
            .assertCount(portCallService, 1);
    }

    @Test
    public void etdAfter_openEnd() {
        newPortCall(null, Timestamp.from(Instant.now()), null, null);

        new PortcallQueryBuilder()
            .etdFrom(ZonedDateTime.now().minus(3, ChronoUnit.DAYS))
            .assertCount(portCallService, 1);
    }

    @Test
    public void etdBefore_openStart() {
        newPortCall(null, Timestamp.from(Instant.now()), null, null);

        new PortcallQueryBuilder()
            .etdTo(ZonedDateTime.now().plus(3, ChronoUnit.DAYS))
            .assertCount(portCallService, 1);
    }

    @Test
    public void etdOutside() {
        newPortCall(null, Timestamp.from(Instant.now()), null, null);

        new PortcallQueryBuilder()
            .etdFrom(ZonedDateTime.now().minus(3, ChronoUnit.DAYS))
            .etdTo(ZonedDateTime.now().minus(2, ChronoUnit.DAYS))
            .assertCount(portCallService, 0);
    }

    @Test
    public void ataBetween() {
        newPortCall(null, null, Timestamp.from(Instant.now()), null);

        new PortcallQueryBuilder()
            .ataFrom(ZonedDateTime.now().minus(3, ChronoUnit.DAYS))
            .ataTo(ZonedDateTime.now().plus(3, ChronoUnit.DAYS))
            .assertCount(portCallService, 1);
    }

    @Test
    public void ataAfter_openEnd() {
        newPortCall(null, null, Timestamp.from(Instant.now()), null);

        new PortcallQueryBuilder()
            .ataFrom(ZonedDateTime.now().minus(3, ChronoUnit.DAYS))
            .assertCount(portCallService, 1);
    }

    @Test
    public void ataBefore_openStart() {
        newPortCall(null, null, Timestamp.from(Instant.now()), null);

        new PortcallQueryBuilder()
            .ataTo(ZonedDateTime.now().plus(3, ChronoUnit.DAYS))
            .assertCount(portCallService, 1);
    }

    @Test
    public void ataOutside() {
        newPortCall(null, null, Timestamp.from(Instant.now()), null);

        new PortcallQueryBuilder()
            .ataFrom(ZonedDateTime.now().minus(3, ChronoUnit.DAYS))
            .ataTo(ZonedDateTime.now().minus(2, ChronoUnit.DAYS))
            .assertCount(portCallService, 0);
    }

    @Test
    public void atdBetween() {
        newPortCall(null, null, null, Timestamp.from(Instant.now()));

        new PortcallQueryBuilder()
            .atdFrom(ZonedDateTime.now().minus(3, ChronoUnit.DAYS))
            .atdTo(ZonedDateTime.now().plus(3, ChronoUnit.DAYS))
            .assertCount(portCallService, 1);
    }

    @Test
    public void atdAfter_openEnd() {
        newPortCall(null, null, null, Timestamp.from(Instant.now()));

        new PortcallQueryBuilder()
            .atdFrom(ZonedDateTime.now().minus(3, ChronoUnit.DAYS))
            .assertCount(portCallService, 1);
    }

    @Test
    public void atdBefore_openStart() {
        newPortCall(null, null, null, Timestamp.from(Instant.now()));

        new PortcallQueryBuilder()
            .atdTo(ZonedDateTime.now().plus(3, ChronoUnit.DAYS))
            .assertCount(portCallService, 1);
    }

    @Test
    public void atdOutside() {
        newPortCall(null, null, null, Timestamp.from(Instant.now()));

        new PortcallQueryBuilder()
            .atdFrom(ZonedDateTime.now().minus(3, ChronoUnit.DAYS))
            .atdTo(ZonedDateTime.now().minus(2, ChronoUnit.DAYS))
            .assertCount(portCallService, 0);
    }

    @Test
    public void modifiedBetween() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .modifiedFrom(ZonedDateTime.now().minus(3, ChronoUnit.HOURS))
            .modifiedTo(ZonedDateTime.now().plus(3, ChronoUnit.HOURS))
            .assertCount(portCallService, 1);
    }

    @Test
    public void modifiedAfter_openEnd() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .modifiedFrom(ZonedDateTime.now().minus(3, ChronoUnit.HOURS))
            .assertCount(portCallService, 1);
    }

    @Test
    public void modifiedBefore_openStart() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .modifiedTo(ZonedDateTime.now().plus(3, ChronoUnit.HOURS))
            .assertCount(portCallService, 1);
    }

    @Test
    public void modifiedOutside() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .modifiedFrom(ZonedDateTime.now().minus(3, ChronoUnit.HOURS))
            .modifiedTo(ZonedDateTime.now().minus(2, ChronoUnit.HOURS))
            .assertCount(portCallService, 0);
    }

    @Test
    public void modifiedAt_notFound() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .modifiedDate(Date.from(ZonedDateTime.now().minusDays(3).toInstant()))
            .assertCount(portCallService, 0);
    }

    @Test
    public void modifiedAt_found() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .modifiedDate(Date.from(ZonedDateTime.now().minusDays(1).toInstant()))
            .assertCount(portCallService, 1);
    }

    @Test
    public void locode_notFound() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .locode("NOT_FOUND")
            .assertCount(portCallService, 0);
    }

    @Test
    public void locode_found() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .locode(PORT_LOCODE)
            .assertCount(portCallService, 1);
    }

    @Test
    public void vesselName_notFound() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .vesselName("NOT_FOUND")
            .assertCount(portCallService, 0);
    }

    @Test
    public void vesselName_found() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .vesselName(VESSEL_NAME)
            .assertCount(portCallService, 1);
    }

    @Test
    public void mmsi_notFound() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .mmsi(1)
            .assertCount(portCallService, 0);
    }

    @Test
    public void mmsi_found() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .mmsi(VESSEL_MMSI)
            .assertCount(portCallService, 1);
    }

    @Test
    public void imo_found() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .imo(VESSEL_IMO)
            .assertCount(portCallService, 1);
    }

    @Test
    public void imo_notFound() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .imo(1)
            .assertCount(portCallService, 0);
    }

    @Test
    public void vesselTypecode_found() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .vesselTypeCode(VESSEL_TYPECODE)
            .assertCount(portCallService, 1);
    }

    @Test
    public void vesselTypecode_notFound() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .vesselTypeCode(1)
            .assertCount(portCallService, 0);
    }

    @Test
    public void nationality_found() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .nationality(List.of(VESSEL_NATIONALITY))
            .assertCount(portCallService, 1);
    }

    @Test
    public void nationality_found_multiple() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .nationality(List.of(VESSEL_NATIONALITY, "TEST"))
            .assertCount(portCallService, 1);
    }

    @Test
    public void nationality_negation_found() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .nationality(List.of("!TEST"))
            .assertCount(portCallService, 1);
    }

    @Test
    public void nationality_negation_notFound() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .nationality(List.of("!" + VESSEL_NATIONALITY))
            .assertCount(portCallService, 0);
    }

    @Test
    public void nationality_notFound() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .nationality(List.of("TEST"))
            .assertCount(portCallService, 0);
    }

    @Test
    public void over1000() {
        IntStream.range(0, 1001).forEach(i -> newPortCall(null, null, null, null));

        new PortcallQueryBuilder()
            .assertException(portCallService);
    }

    @Test
    public void master_is_empty() {
        newPortCall(null, null, null, null);

        final PortCallsJson json = new PortcallQueryBuilder()
            .imo(VESSEL_IMO)
            .assertCount(portCallService, 1);

        assertEquals("", json.portCalls.get(0).getShipMasterArrival());
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
        pc.setVesselName(VESSEL_NAME);
        pc.setPortCallTimestamp(Timestamp.from(Instant.now()));
        pc.setPortToVisit(PORT_LOCODE);
        pc.setMmsi(VESSEL_MMSI);
        pc.setImoLloyds(VESSEL_IMO);
        pc.setNationality(VESSEL_NATIONALITY);
        pc.setVesselTypeCode(VESSEL_TYPECODE);
        pc.setShipMasterArrival(VESSEL_MASTER_NAME);

        portCallRepository.save(pc);
    }

    private static class PortcallQueryBuilder {
        Date modifiedDate= null;
        ZonedDateTime modifiedFrom= null;
        ZonedDateTime modifiedTo= null;
        ZonedDateTime etaFrom= null;
        ZonedDateTime etaTo= null;
        ZonedDateTime etdFrom= null;
        ZonedDateTime etdTo= null;
        ZonedDateTime ataFrom= null;
        ZonedDateTime ataTo= null;
        ZonedDateTime atdFrom= null;
        ZonedDateTime atdTo= null;
        String locode= null;
        String vesselName= null;
        Integer mmsi= null;
        Integer imo= null;
        List<String> nationality= null;
        Integer vesselTypeCode= null;

        public PortCallsJson assertCount(final PortCallService portCallService, final int assertedCount) {
            final PortCallsJson json = portCallService.findPortCalls(modifiedDate, modifiedFrom, modifiedTo, etaFrom, etaTo, etdFrom, etdTo,
                ataFrom, ataTo, atdFrom, atdTo, locode, vesselName, mmsi, imo, nationality, vesselTypeCode);

            assertEquals(assertedCount, json.portCalls.size());

            return json;
        }

        public void assertException(final PortCallService portCallService) {
            Assertions.assertThrows(BadRequestException.class, () -> assertCount(portCallService, 0));
        }

        public PortcallQueryBuilder atdFrom(final ZonedDateTime atdFrom) {
            this.atdFrom = atdFrom;
            return this;
        }

        public PortcallQueryBuilder atdTo(final ZonedDateTime atdTo) {
            this.atdTo = atdTo;
            return this;
        }

        public PortcallQueryBuilder modifiedFrom(final ZonedDateTime modifiedFrom) {
            this.modifiedFrom = modifiedFrom;
            return this;
        }

        public PortcallQueryBuilder modifiedTo(final ZonedDateTime modifiedTo) {
            this.modifiedTo = modifiedTo;
            return this;
        }

        public PortcallQueryBuilder ataFrom(final ZonedDateTime ataFrom) {
            this.ataFrom = ataFrom;
            return this;
        }

        public PortcallQueryBuilder ataTo(final ZonedDateTime ataTo) {
            this.ataTo = ataTo;
            return this;
        }

        public PortcallQueryBuilder etdFrom(final ZonedDateTime etdFrom) {
            this.etdFrom = etdFrom;
            return this;
        }

        public PortcallQueryBuilder etdTo(final ZonedDateTime etdTo) {
            this.etdTo = etdTo;
            return this;
        }

        public PortcallQueryBuilder modifiedDate(final Date modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public PortcallQueryBuilder etaFrom(final ZonedDateTime etaFrom) {
            this.etaFrom = etaFrom;
            return this;
        }

        public PortcallQueryBuilder etaTo(final ZonedDateTime etaTo) {
            this.etaTo = etaTo;
            return this;
        }

        public PortcallQueryBuilder locode(final String locode) {
            this.locode = locode;
            return this;
        }

        public PortcallQueryBuilder vesselName(final String vesselName) {
            this.vesselName = vesselName;
            return this;
        }

        public PortcallQueryBuilder mmsi(final int mmsi) {
            this.mmsi = mmsi;
            return this;
        }

        public PortcallQueryBuilder imo(final int imo) {
            this.imo = imo;
            return this;
        }

        public PortcallQueryBuilder nationality(final List<String> nationality) {
            this.nationality = nationality;
            return this;
        }

        public PortcallQueryBuilder vesselTypeCode(final int vesselTypecode) {
            this.vesselTypeCode = vesselTypecode;
            return this;
        }
    }
}
