package fi.livi.digitraffic.meri.service.portnet;

import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.service.portnet.berth.BerthUpdater;

@Disabled
public class BerthUpdaterTest extends AbstractTestBase {
    @Autowired
    private BerthUpdater berthUpdater;

    @Test
    public void testUpdate() throws IOException {
        berthUpdater.updatePortsAreasAndBerths();
    }
}
