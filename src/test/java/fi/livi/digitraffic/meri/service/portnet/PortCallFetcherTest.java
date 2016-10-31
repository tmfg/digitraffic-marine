package fi.livi.digitraffic.meri.service.portnet;

import java.time.Instant;

import javax.net.ssl.HttpsURLConnection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.AbstractIntegrationTest;
import fi.livi.digitraffic.meri.portnet.xsd.PortCallList;

public class PortCallFetcherTest extends AbstractIntegrationTest {
    @Autowired
    private PortCallFetcher portCallFetcher;

    @Before
    public void disableSslCheck() {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }

    @Test
    public void testGetList() {
        final PortCallList list = portCallFetcher.getList(null, Instant.now());

        Assert.assertNotNull(list);
    }
}
