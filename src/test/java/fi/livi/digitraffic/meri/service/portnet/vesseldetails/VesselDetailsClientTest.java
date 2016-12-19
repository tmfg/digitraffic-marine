package fi.livi.digitraffic.meri.service.portnet.vesseldetails;

import static org.junit.Assert.assertNotNull;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.net.ssl.HttpsURLConnection;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.AbstractIntegrationTest;
import fi.livi.digitraffic.meri.portnet.vesseldetails.xsd.VesselList;

@Ignore("Needs vpn")
public class VesselDetailsClientTest extends AbstractIntegrationTest {

    @Autowired
    private VesselDetailsClient vesselDetailsClient;

    @Before
    public void disableSslCheck() {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }

    @Test
    public void getVesselList() throws Exception {
        VesselList vesselList = vesselDetailsClient.getVesselList(Instant.now().minus(1, ChronoUnit.DAYS));
        assertNotNull(vesselList.getHeader());
    }
}
