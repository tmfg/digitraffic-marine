package fi.livi.digitraffic.meri;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(classes = MarineApplication.class,
                properties = { "config.test=true", "app.type=daemon", "quartz.enabled=false",
                               "spring.main.web-application-type=none" })
public class ContextLoadsDaemonTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    public void contextLoads() {
        assertNotNull(entityManager);
    }
}
