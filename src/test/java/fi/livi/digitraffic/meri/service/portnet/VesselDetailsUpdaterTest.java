package fi.livi.digitraffic.meri.service.portnet;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.AbstractIntegrationTest;
import fi.livi.digitraffic.meri.service.portnet.vesseldetails.VesselDetailsUpdater;

@Ignore("Needs vpn")
public class VesselDetailsUpdaterTest extends AbstractIntegrationTest {

    @Autowired
    private VesselDetailsUpdater vesselDetailsUpdater;

    @Test
    public void updateVesselDetailsSucceeds() {
        vesselDetailsUpdater.update();
    }
}
