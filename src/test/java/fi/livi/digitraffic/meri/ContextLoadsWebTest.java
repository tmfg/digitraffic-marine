package fi.livi.digitraffic.meri;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;

import fi.livi.digitraffic.meri.controller.ais.AisControllerV1;
import jakarta.persistence.EntityManager;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ContextLoadsWebTest extends AbstractWebTestBase {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AisControllerV1 aisControllerV1;

    @Test
    public void contextLoads() {
        assertNotNull(applicationContext);
        assertNotNull(entityManager);
        assertNotNull(aisControllerV1);
    }
}
