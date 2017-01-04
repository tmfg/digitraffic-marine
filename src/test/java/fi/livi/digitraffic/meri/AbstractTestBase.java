package fi.livi.digitraffic.meri;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractTestBase {

    @Autowired
    ResourceLoader resourceLoader;

    protected Resource loadResource(String pattern) throws IOException {
        return resourceLoader.getResource(pattern);
    }

    protected String readResourceContent(String resourcePattern) throws IOException {
        Resource datex2Resource = loadResource(resourcePattern);
        return FileUtils.readFileToString(datex2Resource.getFile(), StandardCharsets.UTF_8);
    }
}