package fi.livi.digitraffic.meri.service.portnet.vesseldetails;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import javax.net.ssl.HttpsURLConnection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.AbstractDaemonTestBase;
import fi.livi.digitraffic.meri.portnet.xsd.VesselList;

@Disabled("Needs vpn")
public class VesselDetailsClientTest extends AbstractDaemonTestBase {

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
