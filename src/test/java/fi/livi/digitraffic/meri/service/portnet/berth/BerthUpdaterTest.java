package fi.livi.digitraffic.meri.service.portnet.berth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.AbstractDaemonTestBase;

@Disabled
public class BerthUpdaterTest extends AbstractDaemonTestBase {
    @Autowired
    private BerthUpdater berthUpdater;

    @Test
    public void testUpdate() {
        Assertions.assertDoesNotThrow(() -> berthUpdater.updatePortsAreasAndBerths());
    }
}
