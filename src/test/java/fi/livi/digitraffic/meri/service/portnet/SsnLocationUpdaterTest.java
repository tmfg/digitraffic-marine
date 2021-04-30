package fi.livi.digitraffic.meri.service.portnet;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.service.portnet.location.SsnLocationUpdater;

public class SsnLocationUpdaterTest extends AbstractTestBase {
    @Autowired
    private SsnLocationUpdater ssnLocationUpdater;

    @Test
    @Transactional(readOnly = false)
    @Rollback(true)
    public void testUpdateLocations() throws IOException, SAXException, URISyntaxException {
        ssnLocationUpdater.updateSsnLocations();
    }
}
