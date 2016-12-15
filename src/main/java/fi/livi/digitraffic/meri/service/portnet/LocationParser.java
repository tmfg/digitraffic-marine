package fi.livi.digitraffic.meri.service.portnet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import org.springframework.util.StringUtils;

/**
 * For parsing locations given in degrees, minutes, seconds format ie. (N)48° 49' 0''
 */
public final class LocationParser {
    private LocationParser() {}

    /**
     * @param value value in degrees, minutes, seconds format ie. (N)48° 49' 0''
     * @return value in decimal degrees format ie. 48.8167 or null if given string value is invalid
     */
    public static Double parseLatitude(final String value) {
        return parse(value, LocationType.LATITUDE);
    }

    /**
     * @param value value in degrees, minutes, seconds format ie. (E)48° 49' 0''
     * @return value in decimal degrees format ie. 48.8167 or null if given string value is invalid
     */
    public static Double parseLongitude(final String value) {
        return parse(value, LocationType.LONGITUDE);
    }

    private enum LocationType {
        LONGITUDE, LATITUDE
    }

    private enum Sign {
        PLUS(1), MINUS(-1), INVALID(0);

        private final int value;

        Sign(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    private static Double parse(final String value, final LocationType type) {
        final String val = StringUtils.trimAllWhitespace(value);

        if (!isValidLength(val)) {
            return null;
        }

        final Sign sign = parseHemisphere(val, type);

        if (sign == Sign.INVALID) {
            return null;
        }

        final int i2 = val.indexOf(176); // °
        final int i3 = val.indexOf('\''); // '
        final int i4 = val.indexOf("\'\'"); // ''

        if (i2 == -1 || i3 == -1 || i4 == -1) {
            return null;
        }

        final String degrees = val.substring(3, i2);
        final String minutes = val.substring(i2 + 1, i3);
        final String seconds = val.substring(i3 + 1, i4);
        final Double dValue = parseValue(degrees, minutes, seconds);

        return parseValueWithSign(dValue, sign);
    }

    private static Double parseValueWithSign(final Double dValue, final Sign sign) {
        if(dValue == null) {
            return null;
        }

        // no sign, when 0.0
        if(Math.abs(dValue.doubleValue()) - 0 < 0.0001) {
            return 0.0;
        }

        return sign.getValue() * dValue;
    }

    private static boolean isValidLength(final String val) {
        // Length of (N)0°0'0'' is 10 chars
        // Length of (N)999°999'999'' is 16 chars
        if (StringUtils.isEmpty(val) || val.length() < 10 || val.length() > 16) {
            return false;
        }
        return true;
    }

    private static Sign parseHemisphere(final String val, final LocationType type) {
        final char hemisphere = val.charAt(1);
        if (type == LocationType.LONGITUDE && Arrays.asList('E', 'W').contains(hemisphere)) {
            return hemisphere == 'E' ? Sign.PLUS : Sign.MINUS;
        } else if (type == LocationType.LATITUDE && Arrays.asList('N', 'S').contains(hemisphere)) {
            return hemisphere == 'N' ? Sign.PLUS : Sign.MINUS;
        }
        return Sign.INVALID;
    }

    private static Double parseValue(final String d, final String m, final String s) {
        try {
            final double degrees = Double.parseDouble(d);
            final double minutes = Double.parseDouble(m);
            final double seconds = Double.parseDouble(s);

            return BigDecimal.valueOf(degrees + minutes / 60 + seconds / 3600)
                .setScale(5, RoundingMode.HALF_UP)
                .doubleValue();
        } catch(final NumberFormatException nfe) {
            return null;
        }
    }
}
