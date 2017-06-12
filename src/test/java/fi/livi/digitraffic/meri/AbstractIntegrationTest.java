package fi.livi.digitraffic.meri;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AisApplication.class,
				webEnvironment = SpringBootTest.WebEnvironment.NONE,
				properties = {"spring.main.web_environment=false", "quartz.enabled=false", "javamelody.enabled=false"})
public abstract class AbstractIntegrationTest extends AbstractTestBase {

	protected String readFile(String filename) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(filename).getFile());

		return FileUtils.readFileToString(file, "UTF-8");
	}

}
