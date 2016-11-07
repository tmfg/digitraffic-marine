package fi.livi.digitraffic.meri.service.portnet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fi.livi.digitraffic.AbstractIntegrationTest;
import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;

public class SsnLocationClientTest extends AbstractIntegrationTest {
    @Autowired
    private SsnLocationClient ssnLocationClient;

    @Test
    public void getSsnLocationsSucceeds() {
        final List<SsnLocation> ssnLocations = ssnLocationClient.getSsnLocations();
        assertTrue(ssnLocations.size() > 15000);

        final SsnLocation ssnLocation = ssnLocations.parallelStream().filter(l -> l.getLocode().equals("DEHEI")).collect(Collectors.toList()).get(0);
        assertEquals("Heidelberg", ssnLocation.getLocationName());
        assertEquals("Germany", ssnLocation.getCountry());
        assertEquals(new BigDecimal("49.4166667"), BigDecimal.valueOf(ssnLocation.getWgs84Lat()).setScale(7, RoundingMode.HALF_UP));
        assertEquals(Double.valueOf("8.7"), ssnLocation.getWgs84Long());
    }
}
