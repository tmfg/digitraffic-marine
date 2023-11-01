package fi.livi.digitraffic.meri.service.portnet.location;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.AbstractDaemonTestBase;

public class SsnLocationUpdaterTest extends AbstractDaemonTestBase {
    @Autowired
    private SsnLocationUpdater ssnLocationUpdater;

    @Test
    @Transactional
    @Rollback
    public void testUpdateLocations() {
        Assertions.assertDoesNotThrow(() -> ssnLocationUpdater.updateSsnLocations());
    }
}
