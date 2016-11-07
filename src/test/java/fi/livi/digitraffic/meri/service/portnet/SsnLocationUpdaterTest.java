package fi.livi.digitraffic.meri.service.portnet;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import fi.livi.digitraffic.AbstractIntegrationTest;

public class SsnLocationUpdaterTest extends AbstractIntegrationTest {
    @Autowired
    private SsnLocationUpdater ssnLocationUpdater;

    @Test
    @Transactional(readOnly = false)
    @Rollback(false)
    public void testUpdateLocations() throws IOException, SAXException, URISyntaxException {

        ssnLocationUpdater.updateSsnLocations();
    }
}
