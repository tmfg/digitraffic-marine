package fi.livi.digitraffic.meri.service.portnet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import fi.livi.digitraffic.AbstractIntegrationTest;

public class SsnLocationUpdaterTest extends AbstractIntegrationTest {
    @Autowired
    private SsnLocationUpdater ssnLocationUpdater;

    public static final String LOCODES_FILE_NAME = "/SSN_LOCODES.xls";

    @Test
    @Transactional(readOnly = false)
    @Rollback(false)
    public void testUpdateLocations() throws IOException, SAXException, OpenXML4JException {
        final Path path = new File(getClass().getResource(LOCODES_FILE_NAME).getFile()).toPath();

        ssnLocationUpdater.updateSsnLocations(path);
    }
}
