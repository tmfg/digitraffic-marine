package fi.livi.digitraffic.meri;

import static java.time.ZoneOffset.UTC;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
                properties = { "quartz.enabled=false", "javamelody.enabled=false", "cache.allowedMmsis = 200" })
@AutoConfigureMockMvc
public abstract class AbstractTestBase {

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    protected MockMvc mockMvc;

    protected String readFile(String filename) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());

        return FileUtils.readFileToString(file, "UTF-8");
    }

    protected Resource loadResource(String pattern) throws IOException {
        return resourceLoader.getResource(pattern);
    }

    protected String readResourceContent(String resourcePattern) throws IOException {
        Resource datex2Resource = loadResource(resourcePattern);
        return FileUtils.readFileToString(datex2Resource.getFile(), StandardCharsets.UTF_8);
    }

    protected void assertTimesEqual(final ZonedDateTime t1, final ZonedDateTime t2) {
        if(t1 == null && t2 == null) return;

        if(t1 == null && t2 != null) {
            Assert.fail("was asserted to be null, was not");
        }

        if(t1 != null && t2 == null) {
            Assert.fail("given value was null");
        }

        final ZonedDateTime tz1 = t1.withZoneSameInstant(UTC);
        final ZonedDateTime tz2 = t2.withZoneSameInstant(UTC);

        Assert.assertEquals(tz1, tz2);
    }
}