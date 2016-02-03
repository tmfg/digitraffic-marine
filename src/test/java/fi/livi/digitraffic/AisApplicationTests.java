package fi.livi.digitraffic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.livi.digitraffic.meri.AisApplication;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AisApplication.class)
@WebAppConfiguration
public class AisApplicationTests {

	@Test
	public void contextLoads() {
	}

}
