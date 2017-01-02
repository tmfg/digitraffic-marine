package fi.livi.digitraffic.meri;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

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