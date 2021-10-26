package fi.livi.digitraffic.meri.service.portnet.vesseldetails;

import static java.time.ZoneOffset.UTC;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import fi.livi.digitraffic.meri.dao.portnet.VesselDetailsRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Transactional
public class VesselDetailsServiceTest extends AbstractTestBase {
    @Autowired
    private VesselDetailsService vesselDetailsService;

    @Autowired
    private EntityManager entityManager;

    private static final String NOT_FOUND = "not_found";
    private static final String VESSEL_NAME = "t1";
    private static final int MMSI_1122 = 1122;
    private static final int MMSI_2222 = 2222;
    private static final int IMO_1234 = 1234;
    private static final int IMO_123 = 123;
    private static final String NATIONALITY_FI = "FI";
    private static final String NATIONALITY_SE = "SE";

    private static final String DATE_1 = "2019-01-1 10:00:00";
    private static final String DATE_2 = "2019-01-10 10:00:00";
    
    @BeforeEach
    public void before() {
        insertVesselDetails(1, MMSI_1122,  IMO_123, VESSEL_NAME, 30, NATIONALITY_FI, DATE_1);
        insertVesselDetails(2, MMSI_2222, IMO_1234 ,"other", 32, NATIONALITY_SE, DATE_2);
    }

    @Test
    public void noArguments() {
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(null, null, null, null, null, null);

        assertThat(vesselDetails, Matchers.hasSize(2));
    }

    @Test
    public void vesselNameFound() {
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(null, VESSEL_NAME, null, null, null, null);

        assertThat(vesselDetails, Matchers.hasSize(1));
    }

    @Test
    public void vesselNameNotFound() {
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(null, NOT_FOUND, null, null, null, null);

        assertThat(vesselDetails, Matchers.empty());
    }

    @Test
    public void vesselMMSIFound() {
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(null, null, MMSI_1122, null, null, null);

        assertThat(vesselDetails, Matchers.hasSize(1));
    }

    @Test
    public void vesselMMSINotFound() {
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(null, null, -1, null, null, null);

        assertThat(vesselDetails, Matchers.empty());
    }

    @Test
    public void vesselIMOFound() {
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(null, null, null, IMO_123, null, null);

        assertThat(vesselDetails, Matchers.hasSize(1));
    }

    @Test
    public void vesselIMONotFound() {
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(null, null, null, -1, null, null);

        assertThat(vesselDetails, Matchers.empty());
    }

    @Test
    public void vesselTimestampBoth() {
        final ZonedDateTime from = LocalDateTime.of(2019, 1, 1, 0, 0, 0).atZone(UTC);
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(from, null, null, null, null, null);

        assertThat(vesselDetails, Matchers.hasSize(2));
    }

    @Test
    public void vesselTimestampOne() {
        final ZonedDateTime from = LocalDateTime.of(2019, 1, 5, 0, 0, 0).atZone(UTC);
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(from, null, null, null, null, null);

        assertThat(vesselDetails, Matchers.hasSize(1));
    }

    @Test
    public void vesselTimestampNone() {
        final ZonedDateTime from = LocalDateTime.of(2019, 1, 11, 0, 0, 0).atZone(UTC);
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(from, null, null, null, null, null);

        assertThat(vesselDetails, Matchers.empty());
    }

    @Test
    public void vesseltypeFound() {
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(null, null, null, null, null, 30);

        assertThat(vesselDetails, Matchers.hasSize(1));
    }

    @Test
    public void vessetypeNotFound() {
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(null, null, null, null, null, -1);

        assertThat(vesselDetails, Matchers.empty());
    }

    @Test
    public void nationalityFound() {
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(null, null, null, null, List.of(NATIONALITY_FI), null);

        assertThat(vesselDetails, Matchers.hasSize(1));
    }

    @Test
    public void nationalityNotFound() {
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(null, null, null, null, List.of("XZ"), null);

        assertThat(vesselDetails, Matchers.empty());
    }

    private void insertVesselDetails(final int vesselId, final int mmsi, final int imo, final String name, final int vesselTypeCode, final String nationality, String date) {
        entityManager.createNativeQuery(String.format("insert into vessel_details(vessel_id,mmsi,name,name_prefix,imo_lloyds,radio_call_sign,radio_call_sign_type,update_timestamp,data_source)\n" +
            "values(%d, %d,'%s','ms',%d,'t1','t1','%s','test')", vesselId, mmsi, name, imo, date)).executeUpdate();

        entityManager.createNativeQuery(String.format("insert into vessel_construction(vessel_id, vessel_type_code, vessel_type_name, ice_class_code)\n" +
            "values (%d, %d, '%s', 1)", vesselId, vesselTypeCode, name)).executeUpdate();

        entityManager.createNativeQuery(String.format("insert into vessel_registration(vessel_id, nationality)\n" +
            "values (%d, '%s')", vesselId, nationality)).executeUpdate();
    }
}
