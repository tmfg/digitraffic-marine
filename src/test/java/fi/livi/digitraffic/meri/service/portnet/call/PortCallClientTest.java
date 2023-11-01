package fi.livi.digitraffic.meri.service.portnet.call;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import javax.net.ssl.HttpsURLConnection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.AbstractDaemonTestBase;
import fi.livi.digitraffic.meri.portnet.xsd.PortCallList;

@Disabled("Needs vpn")
public class PortCallClientTest extends AbstractDaemonTestBase {
    @Autowired
    private PortCallClient portCallClient;

    @BeforeEach
    public void disableSslCheck() {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }

    @Test
    public void testGetList() {
        final PortCallList list = portCallClient.getList(ZonedDateTime.now().minus(1, ChronoUnit.HOURS), ZonedDateTime.now());

        assertNotNull(list);
    }
}
