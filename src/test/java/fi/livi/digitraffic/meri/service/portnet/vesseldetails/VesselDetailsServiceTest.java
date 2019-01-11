package fi.livi.digitraffic.meri.service.portnet.vesseldetails;

import static java.time.ZoneOffset.UTC;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails;

public class VesselDetailsServiceTest extends AbstractTestBase {
    @Autowired
    private VesselDetailsService vesselDetailsService;

    private static final String NOT_FOUND = "not_found";
    private static final String VESSEL_NAME = "t1";
    private static final Integer MMSI = 1122;
    private static final Integer IMO = 123;

    @Test
    public void noArguments() {
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(null, null, null, null, null, null);

        Assert.assertThat(vesselDetails, Matchers.hasSize(2));
    }

    @Test
    public void vesselNameFound() {
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(null, VESSEL_NAME, null, null, null, null);

        Assert.assertThat(vesselDetails, Matchers.hasSize(1));
    }

    @Test
    public void vesselNameNotFound() {
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(null, NOT_FOUND, null, null, null, null);

        Assert.assertThat(vesselDetails, Matchers.empty());
    }

    @Test
    public void vesselMMSIFound() {
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(null, null, MMSI, null, null, null);

        Assert.assertThat(vesselDetails, Matchers.hasSize(1));
    }

    @Test
    public void vesselMMSINotFound() {
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(null, null, -1, null, null, null);

        Assert.assertThat(vesselDetails, Matchers.empty());
    }

    @Test
    public void vesselIMOFound() {
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(null, null, null, IMO, null, null);

        Assert.assertThat(vesselDetails, Matchers.hasSize(1));
    }

    @Test
    public void vesselIMONotFound() {
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(null, null, null, -1, null, null);

        Assert.assertThat(vesselDetails, Matchers.empty());
    }

    @Test
    public void vesselTimestampBoth() {
        final ZonedDateTime from = LocalDateTime.of(2019, 1, 1, 0, 0, 0).atZone(UTC);
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(from, null, null, null, null, null);

        Assert.assertThat(vesselDetails, Matchers.hasSize(2));
    }

    @Test
    public void vesselTimestampOne() {
        final ZonedDateTime from = LocalDateTime.of(2019, 1, 5, 0, 0, 0).atZone(UTC);
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(from, null, null, null, null, null);

        Assert.assertThat(vesselDetails, Matchers.hasSize(1));
    }

    @Test
    public void vesselTimestampNone() {
        final ZonedDateTime from = LocalDateTime.of(2019, 1, 11, 0, 0, 0).atZone(UTC);
        final List<VesselDetails> vesselDetails = vesselDetailsService.findVesselDetails(from, null, null, null, null, null);

        Assert.assertThat(vesselDetails, Matchers.empty());
    }

}
