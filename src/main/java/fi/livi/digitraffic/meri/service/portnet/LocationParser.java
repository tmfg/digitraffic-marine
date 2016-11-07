package fi.livi.digitraffic.meri.service.portnet;

import org.springframework.util.StringUtils;

public final class LocationParser {
    private LocationParser() {}

    public static Double parseLatitude(final String value) {
        return parseString(value);
    }

    public static Double parseLongitude(final String value) {
        return parseString(value);
    }

    private static Double parseString(final String value) {
        if (StringUtils.isEmpty(value) || value.length() < 10) {
            return null;
        }

        final int i2 = value.indexOf(176);
        final int i3 = value.indexOf('\'');
        final int i4 = value.indexOf("\'\'");

        if (i2 == -1 || i3 == -1 || i4 == -1) {
            return null;
        }

        final char hemisphere = value.charAt(1);
        final String s1 = value.substring(3, i2);
        final String s2 = value.substring(i2 + 2, i3);
        final String s3 = value.substring(i3 + 2, i4);

        return parseValue(hemisphere, s1, s2, s3);
    }

    private static double parseValue(final char hemisphere, final String d, final String m, final String s) {
        final double degrees = Integer.valueOf(d);
        final double minutes = Integer.valueOf(m);
        final double seconds = Integer.valueOf(s);

        return (hemisphere == 'N' || hemisphere == 'E' ? 1 : -1) * (degrees + minutes / 60 + seconds / 3600);
    }
}
