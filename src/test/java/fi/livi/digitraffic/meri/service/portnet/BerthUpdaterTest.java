package fi.livi.digitraffic.meri.service.portnet;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.service.portnet.berth.BerthUpdater;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Disabled
public class BerthUpdaterTest extends AbstractTestBase {
    @Autowired
    private BerthUpdater berthUpdater;

    @Test
    public void testUpdate() {
        Assertions.assertDoesNotThrow(() -> berthUpdater.updatePortsAreasAndBerths());
    }
}
