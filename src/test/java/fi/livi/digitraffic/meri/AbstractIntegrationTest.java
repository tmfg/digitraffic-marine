package fi.livi.digitraffic.meri;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AisApplication.class,
				webEnvironment = SpringBootTest.WebEnvironment.NONE,
				properties = {"spring.main.web_environment=false", "quartz.enabled=false"})
public abstract class AbstractIntegrationTest extends AbstractTestBase {

}
