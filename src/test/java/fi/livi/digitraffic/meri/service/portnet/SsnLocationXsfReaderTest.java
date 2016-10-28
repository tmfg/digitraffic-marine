package fi.livi.digitraffic.meri.service.portnet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.xml.sax.SAXException;

import fi.livi.digitraffic.meri.AisApplication;
import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AisApplication.class)
@WebAppConfiguration
public class SsnLocationXsfReaderTest {
    @Autowired
    private SsnLocationXsfReader ssnLocationXsfReader;

    public static final String LOCODES_FILE_NAME = "/SSN_LOCODES.xls";

    @Test
    public void testRead() throws OpenXML4JException, SAXException, IOException {
        final Path path = new File(getClass().getResource(LOCODES_FILE_NAME).getFile()).toPath();

        final List<SsnLocation> locations = ssnLocationXsfReader.readLocations(path);
        Assert.assertThat(locations, Matchers.not(Matchers.empty()));
    }
}
