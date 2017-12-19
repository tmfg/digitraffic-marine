package fi.livi.digitraffic.meri.service.portnet;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import javax.net.ssl.HttpsURLConnection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.AbstractIntegrationTest;
import fi.livi.digitraffic.meri.portnet.xsd.PortCallList;

@Ignore("Needs vpn")
public class PortCallClientTest extends AbstractIntegrationTest {
    @Autowired
    private PortCallClient portCallClient;

    @Before
    public void disableSslCheck() {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }

    @Test
    public void testGetList() {
        final PortCallList list = portCallClient.getList(ZonedDateTime.now().minus(1, ChronoUnit.HOURS), ZonedDateTime.now());

        Assert.assertNotNull(list);
    }
}
