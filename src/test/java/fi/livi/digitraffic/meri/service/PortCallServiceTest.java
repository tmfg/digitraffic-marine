package fi.livi.digitraffic.meri.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.domain.portnet.PortAreaDetails;
import fi.livi.digitraffic.meri.domain.portnet.PortCall;
import fi.livi.digitraffic.meri.dto.portcall.v1.PortCallsV1;
import fi.livi.digitraffic.meri.service.portcall.PortCallServiceV1;

@Transactional
public class PortCallServiceTest extends AbstractTestBase {

    @Autowired
    private PortCallServiceV1 portCallServiceV1;

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
            .etaFrom(Instant.now().minus(3, ChronoUnit.DAYS))
            .etaTo(Instant.now().plus(3, ChronoUnit.DAYS))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void etaAfter_openEnd() {
        newPortCall(Timestamp.from(Instant.now()), null, null, null);

        new PortcallQueryBuilder()
            .etaFrom(Instant.now().minus(3, ChronoUnit.DAYS))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void etaBefore_openStart() {
        newPortCall(Timestamp.from(Instant.now()), null, null, null);

        new PortcallQueryBuilder()
            .etaTo(Instant.now().plus(3, ChronoUnit.DAYS))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void etaOutside() {
        newPortCall(Timestamp.from(Instant.now()), null, null, null);

        new PortcallQueryBuilder()
            .etaFrom(Instant.now().minus(3, ChronoUnit.DAYS))
            .etaTo(Instant.now().minus(2, ChronoUnit.DAYS))
            .assertCount(portCallServiceV1, 0);
    }

    @Test
    public void etdBetween() {
        newPortCall(null, Timestamp.from(Instant.now()), null, null);

        new PortcallQueryBuilder()
            .etdFrom(Instant.now().minus(3, ChronoUnit.DAYS))
            .etdTo(Instant.now().plus(3, ChronoUnit.DAYS))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void etdAfter_openEnd() {
        newPortCall(null, Timestamp.from(Instant.now()), null, null);

        new PortcallQueryBuilder()
            .etdFrom(Instant.now().minus(3, ChronoUnit.DAYS))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void etdBefore_openStart() {
        newPortCall(null, Timestamp.from(Instant.now()), null, null);

        new PortcallQueryBuilder()
            .etdTo(Instant.now().plus(3, ChronoUnit.DAYS))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void etdOutside() {
        newPortCall(null, Timestamp.from(Instant.now()), null, null);

        new PortcallQueryBuilder()
            .etdFrom(Instant.now().minus(3, ChronoUnit.DAYS))
            .etdTo(Instant.now().minus(2, ChronoUnit.DAYS))
            .assertCount(portCallServiceV1, 0);
    }

    @Test
    public void ataBetween() {
        newPortCall(null, null, Timestamp.from(Instant.now()), null);

        new PortcallQueryBuilder()
            .ataFrom(Instant.now().minus(3, ChronoUnit.DAYS))
            .ataTo(Instant.now().plus(3, ChronoUnit.DAYS))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void ataBetweenAndVesselName() {
        newPortCall(null, null, Timestamp.from(Instant.now()), null);

        new PortcallQueryBuilder()
            .vesselName(VESSEL_NAME)
            .ataFrom(Instant.now().minus(3, ChronoUnit.DAYS))
            .ataTo(Instant.now().plus(3, ChronoUnit.DAYS))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void ataBetweenAndPortToVisit() {
        newPortCall(null, null, Timestamp.from(Instant.now()), null);

        new PortcallQueryBuilder()
            .locode(PORT_LOCODE)
            .ataFrom(Instant.now().minus(3, ChronoUnit.DAYS))
            .ataTo(Instant.now().plus(3, ChronoUnit.DAYS))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void ataAfter_openEnd() {
        newPortCall(null, null, Timestamp.from(Instant.now()), null);

        new PortcallQueryBuilder()
            .ataFrom(Instant.now().minus(3, ChronoUnit.DAYS))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void ataBefore_openStart() {
        newPortCall(null, null, Timestamp.from(Instant.now()), null);

        new PortcallQueryBuilder()
            .ataTo(Instant.now().plus(3, ChronoUnit.DAYS))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void ataOutside() {
        newPortCall(null, null, Timestamp.from(Instant.now()), null);

        new PortcallQueryBuilder()
            .ataFrom(Instant.now().minus(3, ChronoUnit.DAYS))
            .ataTo(Instant.now().minus(2, ChronoUnit.DAYS))
            .assertCount(portCallServiceV1, 0);
    }

    @Test
    public void atdBetween() {
        newPortCall(null, null, null, Timestamp.from(Instant.now()));

        new PortcallQueryBuilder()
            .atdFrom(Instant.now().minus(3, ChronoUnit.DAYS))
            .atdTo(Instant.now().plus(3, ChronoUnit.DAYS))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void atdAfter_openEnd() {
        newPortCall(null, null, null, Timestamp.from(Instant.now()));

        new PortcallQueryBuilder()
            .atdFrom(Instant.now().minus(3, ChronoUnit.DAYS))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void atdBefore_openStart() {
        newPortCall(null, null, null, Timestamp.from(Instant.now()));

        new PortcallQueryBuilder()
            .atdTo(Instant.now().plus(3, ChronoUnit.DAYS))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void atdOutside() {
        newPortCall(null, null, null, Timestamp.from(Instant.now()));

        new PortcallQueryBuilder()
            .atdFrom(Instant.now().minus(3, ChronoUnit.DAYS))
            .atdTo(Instant.now().minus(2, ChronoUnit.DAYS))
            .assertCount(portCallServiceV1, 0);
    }

    @Test
    public void modifiedBetween() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .modifiedFrom(Instant.now().minus(3, ChronoUnit.HOURS))
            .modifiedTo(Instant.now().plus(3, ChronoUnit.HOURS))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void modifiedAfter_openEnd() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .modifiedFrom(Instant.now().minus(3, ChronoUnit.HOURS))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void modifiedBefore_openStart() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .modifiedTo(Instant.now().plus(3, ChronoUnit.HOURS))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void modifiedOutside() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .modifiedFrom(Instant.now().minus(3, ChronoUnit.HOURS))
            .modifiedTo(Instant.now().minus(2, ChronoUnit.HOURS))
            .assertCount(portCallServiceV1, 0);
    }

    @Test
    public void modifiedAt_notFound() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .modifiedDate(Date.from(Instant.now().minus(3, ChronoUnit.DAYS)))
            .assertCount(portCallServiceV1, 0);
    }

    @Test
    public void modifiedAt_found() {
        newPortCall(null, null, null, null);
        entityManager.flush();
        entityManager.clear();
        new PortcallQueryBuilder()
            .modifiedDate(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void locode_notFound() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .locode("NOT_FOUND")
            .assertCount(portCallServiceV1, 0);
    }

    @Test
    public void locode_found() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .locode(PORT_LOCODE)
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void vesselName_notFound() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .vesselName("NOT_FOUND")
            .assertCount(portCallServiceV1, 0);
    }

    @Test
    public void vesselName_found() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .vesselName(VESSEL_NAME)
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void mmsi_notFound() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .mmsi(1)
            .assertCount(portCallServiceV1, 0);
    }

    @Test
    public void mmsi_found() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .mmsi(VESSEL_MMSI)
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void imo_found() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .imo(VESSEL_IMO)
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void imo_notFound() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .imo(1)
            .assertCount(portCallServiceV1, 0);
    }

    @Test
    public void vesselTypecode_found() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .vesselTypeCode(VESSEL_TYPECODE)
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void vesselTypecode_notFound() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .vesselTypeCode(1)
            .assertCount(portCallServiceV1, 0);
    }

    @Test
    public void nationality_found() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .nationality(List.of(VESSEL_NATIONALITY))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void nationality_found_multiple() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .nationality(List.of(VESSEL_NATIONALITY, "TEST"))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void nationality_negation_found() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .nationality(List.of("!TEST"))
            .assertCount(portCallServiceV1, 1);
    }

    @Test
    public void nationality_negation_notFound() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .nationality(List.of("!" + VESSEL_NATIONALITY))
            .assertCount(portCallServiceV1, 0);
    }

    @Test
    public void nationality_notFound() {
        newPortCall(null, null, null, null);

        new PortcallQueryBuilder()
            .nationality(List.of("TEST"))
            .assertCount(portCallServiceV1, 0);
    }

    @Test
    public void over1000() {
        IntStream.range(0, 1001).forEach(i -> newPortCall(null, null, null, null));

        new PortcallQueryBuilder()
            .assertException(portCallServiceV1);
    }

    @Test
    public void master_is_empty() {
        newPortCall(null, null, null, null);

        final PortCallsV1 json = new PortcallQueryBuilder()
            .imo(VESSEL_IMO)
            .assertCount(portCallServiceV1, 1);

        assertEquals(StringUtils.EMPTY, json.portCalls.get(0).getShipMasterArrival());
        assertEquals(StringUtils.EMPTY, json.portCalls.get(0).getShipMasterDeparture());
    }

    private static final AtomicLong portCallId = new AtomicLong(1L);

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
        pc.setPortCallId(portCallId.getAndIncrement());
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
        pc.setShipMasterDeparture(VESSEL_MASTER_NAME);

        portCallRepository.save(pc);
    }

    private static class PortcallQueryBuilder {
        Date modifiedDate = null;
        Instant modifiedFrom = null;
        Instant modifiedTo = null;
        Instant etaFrom = null;
        Instant etaTo = null;
        Instant etdFrom = null;
        Instant etdTo = null;
        Instant ataFrom = null;
        Instant ataTo = null;
        Instant atdFrom = null;
        Instant atdTo = null;
        String locode = null;
        String vesselName = null;
        Integer mmsi = null;
        Integer imo = null;
        List<String> nationality = null;
        Integer vesselTypeCode = null;

        public PortCallsV1 assertCount(final PortCallServiceV1 portCallServiceV1, final int assertedCount) {
            final PortCallsV1 json = portCallServiceV1.findPortCalls(modifiedDate, modifiedFrom, modifiedTo, etaFrom, etaTo, etdFrom, etdTo,
                ataFrom, ataTo, atdFrom, atdTo, locode, vesselName, mmsi, imo, nationality, vesselTypeCode);

            assertEquals(assertedCount, json.portCalls.size());

            return json;
        }

        public void assertException(final PortCallServiceV1 portCallServiceV1) {
            Assertions.assertThrows(BadRequestException.class, () -> assertCount(portCallServiceV1, 0));
        }

        public PortcallQueryBuilder atdFrom(final Instant atdFrom) {
            this.atdFrom = atdFrom;
            return this;
        }

        public PortcallQueryBuilder atdTo(final Instant atdTo) {
            this.atdTo = atdTo;
            return this;
        }

        public PortcallQueryBuilder modifiedFrom(final Instant modifiedFrom) {
            this.modifiedFrom = modifiedFrom;
            return this;
        }

        public PortcallQueryBuilder modifiedTo(final Instant modifiedTo) {
            this.modifiedTo = modifiedTo;
            return this;
        }

        public PortcallQueryBuilder ataFrom(final Instant ataFrom) {
            this.ataFrom = ataFrom;
            return this;
        }

        public PortcallQueryBuilder ataTo(final Instant ataTo) {
            this.ataTo = ataTo;
            return this;
        }

        public PortcallQueryBuilder etdFrom(final Instant etdFrom) {
            this.etdFrom = etdFrom;
            return this;
        }

        public PortcallQueryBuilder etdTo(final Instant etdTo) {
            this.etdTo = etdTo;
            return this;
        }

        public PortcallQueryBuilder modifiedDate(final Date modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public PortcallQueryBuilder etaFrom(final Instant etaFrom) {
            this.etaFrom = etaFrom;
            return this;
        }

        public PortcallQueryBuilder etaTo(final Instant etaTo) {
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
