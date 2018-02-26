package fi.livi.digitraffic.meri.util;

import java.time.ZonedDateTime;

public final class TimeUtil {
    private TimeUtil() {}

    public static long millisBetween(final ZonedDateTime t1, final ZonedDateTime t2) {
        if(t1 == null || t2 == null) {
            throw new IllegalArgumentException("null values");
        }

        return t1.toInstant().toEpochMilli() - t2.toInstant().toEpochMilli();
    }
}
