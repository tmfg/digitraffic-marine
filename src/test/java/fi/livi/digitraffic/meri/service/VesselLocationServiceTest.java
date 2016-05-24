package fi.livi.digitraffic.meri.service;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.common.collect.Iterables;

import fi.livi.digitraffic.meri.AisApplication;
import fi.livi.digitraffic.meri.domain.VesselLocation;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AisApplication.class)
@WebAppConfiguration
public class VesselLocationServiceTest {
    @Autowired
    private VesselLocationService vesselLocationService;

    @Test
    public void test() {
        final Iterable<VesselLocation> locations = vesselLocationService.findLocations(null, null);
        assertNotNull(locations);
        assertTrue(Iterables.isEmpty(locations));
    }
}
