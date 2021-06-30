package fi.livi.digitraffic.meri.service.portnet;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.service.portnet.location.SsnLocationUpdater;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

public class SsnLocationUpdaterTest extends AbstractTestBase {
    @Autowired
    private SsnLocationUpdater ssnLocationUpdater;

    @Test
    @Transactional
    @Rollback
    public void testUpdateLocations() {
        Assertions.assertDoesNotThrow(() -> ssnLocationUpdater.updateSsnLocations());
    }
}
