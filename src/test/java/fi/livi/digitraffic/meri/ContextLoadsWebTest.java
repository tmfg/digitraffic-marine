package fi.livi.digitraffic.meri;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(classes = MarineApplication.class,
                properties = { "config.test=true", "app.type=web",
                               "spring.main.web-application-type=servlet" })
public class ContextLoadsWebTest extends AbstractTestBase {
    @Test
    public void contextLoads() {
    }

}
