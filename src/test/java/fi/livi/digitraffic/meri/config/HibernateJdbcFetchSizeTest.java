package fi.livi.digitraffic.meri.config;

import fi.livi.digitraffic.meri.AbstractDaemonTestBase;
import fi.livi.digitraffic.meri.dao.portnet.SsnLocationRepository;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies that Hibernate applies hibernate.jdbc.fetch_size to queries.  Must be run manually
 */
@TestPropertySource(properties = {
    "logging.level.org.hibernate.SQL=DEBUG",
    "logging.level.org.hibernate.orm.jdbc.bind=TRACE",
    "logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE",
    "spring.jpa.properties.hibernate.jdbc.fetch_size=1000"
})
// one sample of timing with different fetch_sizes
// 1    -> 2560440416
// 10   -> 409592583
// 100  -> 187608750
// 1000 -> 146442375
@Disabled
public class HibernateJdbcFetchSizeTest extends AbstractDaemonTestBase {
    @Autowired
    private SsnLocationRepository ssnLocationRepository;

    @Test
    @Transactional
    public void hibernateSetsConfiguredFetchSizeOnEveryStatement() {
        final var stopWatch = StopWatch.createStarted();
        // Trigger a real Hibernate query so Hibernate prepares its own JDBC statement
        final var locations = ssnLocationRepository.findAll();

        assertTrue(locations.size() > 10);
        System.out.println("Took time " + stopWatch.getNanoTime());
    }
}

