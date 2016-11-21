package fi.livi.digitraffic.meri.service.portnet;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

public class LocationParserTest {

    @Test
    public void parseLatitudeSucceeds() {

        Double latitude = LocationParser.parseLatitude("(N)44° 52' 0'';foobar");
        assertCoordinates(44.86667, latitude);

        latitude = LocationParser.parseLatitude("(S)44° 52' 0''");
        assertCoordinates(-44.86667, latitude);

        latitude = LocationParser.parseLatitude("(N)100° 45' 95''");
        assertCoordinates(100.7763889, latitude);

        latitude = LocationParser.parseLatitude("(S)100° 45' 95''");
        assertCoordinates(-100.7763889, latitude);
    }

    @Test
    public void parseLongitudeSucceeds() {

        Double longitude = LocationParser.parseLongitude("(W)0° 37' 0'';foobar");
        assertCoordinates(-0.616667, longitude);

        longitude = LocationParser.parseLongitude("(E)0° 37' 0''");
        assertCoordinates(0.616667, longitude);

        longitude = LocationParser.parseLongitude("(W)666° 66' 666''");
        assertCoordinates(-667.285, longitude);

        longitude = LocationParser.parseLongitude("(E)666° 66' 666''");
        assertCoordinates(667.285, longitude);
    }

    private void assertCoordinates(double val1, double val2) {
        assertEquals(BigDecimal.valueOf(val1).setScale(5, RoundingMode.HALF_UP), BigDecimal.valueOf(val2).setScale(5, RoundingMode.HALF_UP));
    }
}
