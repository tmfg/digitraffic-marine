package fi.livi.digitraffic.meri;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AisApplication.class,
				webEnvironment = SpringBootTest.WebEnvironment.NONE,
				properties = {"spring.main.web_environment=false", "quartz.enabled=false"})
public abstract class AbstractIntegrationTest extends AbstractTestBase {

	protected String readFile(String filename) throws FileNotFoundException {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(filename).getFile());

		Scanner scanner = new Scanner(file);
		String response = "";
		while (scanner.hasNextLine()) {
			response = response + scanner.nextLine();
		}
		return response;
	}

}
