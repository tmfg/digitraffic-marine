package fi.livi.digitraffic.meri.service.portnet;

import javax.net.ssl.HttpsURLConnection;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.AbstractIntegrationTest;

@Ignore("Needs vpn")
public class PortCallUpdaterTest extends AbstractIntegrationTest {
    @Autowired
    private PortCallUpdater portCallUpdater;

    @Before
    public void disableSslCheck() {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }

    @Test
    public void testUpdate() {
        portCallUpdater.update();
    }
}
