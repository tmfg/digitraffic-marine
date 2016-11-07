package fi.livi.digitraffic.meri.service.portnet;

import fi.livi.digitraffic.meri.AisApplication;
import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AisApplication.class)
@WebAppConfiguration
public class SsnLocationClientTest {

    @Autowired
    private SsnLocationClient ssnLocationClient;

    @Test
    public void getSsnLocationsSucceeds() {

        List<SsnLocation> ssnLocations = ssnLocationClient.getSsnLocations();

        assertTrue(ssnLocations.size() > 15000);
        SsnLocation ssnLocation = ssnLocations.parallelStream().filter(l -> l.getLocode().equals("DEHEI")).collect(Collectors.toList()).get(0);
        assertEquals("Heidelberg", ssnLocation.getLocationName());
        assertEquals("Germany", ssnLocation.getCountry());
        assertEquals(new BigDecimal("49.4166667"), BigDecimal.valueOf(ssnLocation.getWgs84Lat()).setScale(7, RoundingMode.HALF_UP));
        assertEquals(Double.valueOf("8.7"), ssnLocation.getWgs84Long());
    }
}
