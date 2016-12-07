package fi.livi.digitraffic;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.livi.digitraffic.meri.AisApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AisApplication.class,
				webEnvironment = SpringBootTest.WebEnvironment.NONE,
				properties = {"spring.main.web_environment=false", "quartz.enabled=false"})
public abstract class AbstractIntegrationTest {

}
