package fi.livi.digitraffic.meri.util;

import static java.time.temporal.ChronoField.MILLI_OF_SECOND;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class TimeUtil {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");

    public static final ZoneId FINLAND_ZONE = ZoneId.of("Europe/Helsinki");

    private TimeUtil() {}

    public static long millisBetween(final ZonedDateTime t1, final ZonedDateTime t2) {
        if(t1 == null || t2 == null) {
            throw new IllegalArgumentException("null values");
        }

        return t1.toInstant().toEpochMilli() - t2.toInstant().toEpochMilli();
    }

    /**
     * Returns given time as a string(yyyyMMdd).  This will happen in the timezone the given
     * time is!  There will be no zone information in the returned string.
     */
    public static String dateToString(final String datePrefix, final ZonedDateTime timestamp) {
        return timestamp == null ? "" : String.format("%s=%s", datePrefix, timestamp.format(DATE_FORMATTER));
    }

    /**
     * Returns given time as a string(HHmmss).  This will happen in the timezone the given
     * time is!  There will be no zone information in the returned string.
     */
    public static String timeToString(final String timePrefix, final ZonedDateTime timestamp) {
        return timestamp == null ? "" : String.format("%s=%s", timePrefix, timestamp.format(TIME_FORMATTER));
    }

    public static Instant toInstant(final ZonedDateTime from) {
        return from != null ? from.toInstant() : null;
    }

    public static ZonedDateTime toZonedDateTime(final Instant from) {
        return from != null ? from.atZone(ZoneOffset.UTC) : null;
    }

    public static Instant withoutMillis(final Instant from) {
        return from != null ? from.with(MILLI_OF_SECOND, 0) : null;
    }
}
