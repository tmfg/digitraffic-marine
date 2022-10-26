package fi.livi.digitraffic.meri.util;

import static java.time.temporal.ChronoField.MILLI_OF_SECOND;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class TimeUtil {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");

    public static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    public static final DateTimeFormatter HTTP_DATE_FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;

    public static final ZoneId FINLAND_ZONE = ZoneId.of("Europe/Helsinki");
    public static final ZoneId GMT_ZONE = ZoneId.of("GMT");

    public static final String LAST_MODIFIED_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static final DateTimeFormatter LAST_MODIFIED_FORMATTER =
        DateTimeFormatter.ofPattern(LAST_MODIFIED_FORMAT, Locale.US).withZone(GMT_ZONE);


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

    public static Instant withoutMillis(final Instant from) {
        return from != null ? from.with(MILLI_OF_SECOND, 0) : null;
    }

    /**
     * Convert from "YYYY-MM-DD" to "EEE, dd MMM yyyy HH:mm:ss z"
     */
    public static String isoLocalDateToHttpDateTime(final String isoLocalDate) {
        final LocalDate parsedDate = LocalDate.parse(isoLocalDate, ISO_DATE_FORMATTER);
        return HTTP_DATE_FORMATTER.format(parsedDate.atStartOfDay(GMT_ZONE));
    }

    public static String getInLastModifiedHeaderFormat(final Instant instant) {
        return LAST_MODIFIED_FORMATTER.format(instant);
    }

    public static Instant getGreatest(final Instant first, final Instant second) {
        if (first == null) {
            return second;
        } else if(second == null || first.isAfter(second)) {
            return first;
        }
        return second;
    }
}
