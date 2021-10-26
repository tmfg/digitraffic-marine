package fi.livi.digitraffic.meri;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestPropertySource(properties = { "app.type=daemon", "spring.main.web-application-type=none" })
public class ContextLoadsDaemonTest extends AbstractTestBase {

    @Autowired
    private EntityManager entityManager;

    @Test
    public void contextLoads() {
        assertNotNull(entityManager);
    }
}
