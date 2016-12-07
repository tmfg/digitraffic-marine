package fi.livi.digitraffic.meri.service.portnet;

import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.AbstractIntegrationTest;
import fi.livi.digitraffic.meri.service.portnet.berth.BerthUpdater;

public class BerthUpdaterTest extends AbstractIntegrationTest {
    @Autowired
    private BerthUpdater berthUpdater;

    @Test
    public void testUpdate() throws IOException {
        berthUpdater.updatePortsAreasAndBerths();
    }
}
