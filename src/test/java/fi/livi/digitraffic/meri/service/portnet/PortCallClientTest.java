package fi.livi.digitraffic.meri.service.portnet;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.portnet.xsd.PortCallList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.HttpsURLConnection;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled("Needs vpn")
public class PortCallClientTest extends AbstractTestBase {
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
