package fi.livi.digitraffic.meri.service.portnet;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.service.portnet.berth.BerthUpdater;

@Ignore
public class BerthUpdaterTest extends AbstractTestBase {
    @Autowired
    private BerthUpdater berthUpdater;

    @Test
    public void testUpdate() throws IOException {
        berthUpdater.updatePortsAreasAndBerths();
    }
}
