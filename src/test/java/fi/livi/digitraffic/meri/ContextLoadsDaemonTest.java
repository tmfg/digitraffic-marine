package fi.livi.digitraffic.meri;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import fi.livi.digitraffic.meri.controller.ais.AisControllerV1;
import jakarta.persistence.EntityManager;

public class ContextLoadsDaemonTest extends AbstractDaemonTestBase {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    private AisControllerV1 aisControllerV1;

    @Test
    public void contextLoads() {
        assertNotNull(applicationContext);
        assertNotNull(entityManager);
        assertNull(aisControllerV1);
    }
}
