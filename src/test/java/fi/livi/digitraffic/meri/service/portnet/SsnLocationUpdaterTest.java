package fi.livi.digitraffic.meri.service.portnet;

import fi.livi.digitraffic.AbstractIntegrationTest;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URISyntaxException;

public class SsnLocationUpdaterTest extends AbstractIntegrationTest {
    @Autowired
    private SsnLocationUpdater ssnLocationUpdater;

    @Test
    @Transactional(readOnly = false)
    @Rollback(false)
    public void testUpdateLocations() throws IOException, SAXException, OpenXML4JException, URISyntaxException {

        ssnLocationUpdater.updateSsnLocations();
    }
}
