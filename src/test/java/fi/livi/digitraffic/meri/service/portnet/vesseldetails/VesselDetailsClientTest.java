package fi.livi.digitraffic.meri.service.portnet.vesseldetails;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import javax.net.ssl.HttpsURLConnection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.portnet.vesseldetails.xsd.VesselList;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled("Needs vpn")
public class VesselDetailsClientTest extends AbstractTestBase {

    @Autowired
    private VesselDetailsClient vesselDetailsClient;

    @BeforeEach
    public void disableSslCheck() {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }

    @Test
    public void getVesselList() {
        final VesselList vesselList = vesselDetailsClient.getVesselList(ZonedDateTime.now().minus(1, ChronoUnit.DAYS));
        assertNotNull(vesselList.getHeader());
    }
}
