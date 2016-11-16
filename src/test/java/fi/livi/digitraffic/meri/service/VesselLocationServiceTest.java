package fi.livi.digitraffic.meri.service;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.common.collect.Iterables;

import fi.livi.digitraffic.meri.AisApplication;
import fi.livi.digitraffic.meri.model.ais.VesselLocationJson;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AisApplication.class)
@WebAppConfiguration
public class VesselLocationServiceTest {
    @Autowired
    private VesselLocationService vesselLocationService;

    @Test
    public void test() {
        final Iterable<VesselLocationJson> locations = vesselLocationService.findLocations(null, null);
        assertNotNull(locations);
        assertTrue(Iterables.isEmpty(locations));
    }
}
