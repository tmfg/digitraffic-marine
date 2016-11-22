package fi.livi.digitraffic.meri.service.portnet;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Assert;
import org.junit.Test;

public class LocationParserTest {

    @Test
    public void parseLatitudeSucceeds() {
        assertLatitude("(N)44° 52' 0'';foobar", 44.86667); // should this be error?
        assertLatitude("(S)44° 52' 0''", -44.86667);
        assertLatitude("(N)100° 45' 95''", 100.7763889);
        assertLatitude("(S)100° 45' 95''", -100.7763889);
        assertLatitude("(Z)100° 45' 95''", -100.7763889); // should this be error?
    }

    @Test
    public void parseLongitudeSucceeds() {
        assertLongitude("(W)0° 37' 0'';foobar", -0.616667); // should this be error?
        assertLongitude("(E)0° 37' 0''", 0.616667);
        assertLongitude("(W)666° 66' 666''", -667.285);
        assertLongitude("(E)666° 66' 666''", 667.285);
        assertLongitude("(Z)666° 66' 666''", -667.285); // should this be error?
    }

    @Test
    public void latitudeDegreeMissing() {
        assertLatitudeFails("(Whassu");
    }

    @Test
    public void latitudeMinutesMissing() {
        assertLatitudeFails("(W)° 12");
    }

    @Test
    public void latitudeSecondsMissing() {
        assertLatitudeFails("(W)° 12' 123");
    }

    @Test
    public void tooLong() {
        assertLatitudeFails("(E)99999999999° 37' 0''");
    }

    private void assertLatitude(final String s, final double expected) {
        final Double parsed = LocationParser.parseLatitude(s);
        assertCoordinates(expected, parsed);
    }

    private void assertLongitude(final String s, final double expected) {
        final Double parsed = LocationParser.parseLongitude(s);
        assertCoordinates(expected, parsed);
    }

    private void assertLatitudeFails(final String s) {
        Assert.assertNull(LocationParser.parseLatitude(s));
    }

    private void assertCoordinates(double val1, double val2) {
        assertEquals(BigDecimal.valueOf(val1).setScale(5, RoundingMode.HALF_UP), BigDecimal.valueOf(val2).setScale(5, RoundingMode.HALF_UP));
    }
}
