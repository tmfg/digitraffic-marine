package fi.livi.digitraffic.meri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(classes = MarineApplication.class,
                properties = { "config.test=true", "app.type=daemon", "quartz.enabled=false",
                               "spring.main.web-application-type=none" })
public class ContextLoadsDaemonTest {

    @Test
    public void contextLoads() {
    }

}
