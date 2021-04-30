package fi.livi.digitraffic.meri;

import org.apache.commons.io.FileUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                properties = { "quartz.enabled=false", "cache.allowedMmsis = 200" })
@AutoConfigureMockMvc
public abstract class AbstractTestBase {

    @Autowired
    protected ResourceLoader resourceLoader;

    @Autowired
    protected MockMvc mockMvc;

    protected String readFile(final String filename) throws IOException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource(filename).getFile());

        return FileUtils.readFileToString(file, "UTF-8");
    }

    protected Resource loadResource(final String pattern) throws IOException {
        return resourceLoader.getResource(pattern);
    }

    protected String readResourceContent(final String resourcePattern) throws IOException {
        final Resource datex2Resource = loadResource(resourcePattern);
        return FileUtils.readFileToString(datex2Resource.getFile(), StandardCharsets.UTF_8);
    }

    protected void assertTimesEqual(final ZonedDateTime t1, final ZonedDateTime t2) {
        if(t1 == null && t2 == null) return;

        if(t1 == null && t2 != null) {
            fail("was asserted to be null, was not");
        }

        if(t1 != null && t2 == null) {
            fail("given value was null");
        }

        final ZonedDateTime tz1 = t1.withZoneSameInstant(UTC);
        final ZonedDateTime tz2 = t2.withZoneSameInstant(UTC);

        assertEquals(tz1, tz2);
    }
}