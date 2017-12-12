package fi.livi.digitraffic.meri.service.winternavigation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.AbstractIntegrationTest;
import ibnet_baltice_ports.Ports;

public class WinterNavigationIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private WinterNavigationClient winterNavigationClient;

    @Test
    @Ignore("For manual integration testing")
    public void getWinterNavigationPortsSucceeds() throws IOException {

        final Ports ports = winterNavigationClient.getWinterNavigationPorts();

        assertNotNull(ports);
        assertTrue(ports.getPort().size() > 100);
    }
}
