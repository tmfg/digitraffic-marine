package fi.livi.digitraffic.util;

import java.util.EnumSet;

import org.springframework.http.HttpStatus;

public final class RestUtil {
    private RestUtil() {}

    private static final EnumSet<HttpStatus.Series> ERRORS = EnumSet.of(HttpStatus.Series.CLIENT_ERROR, HttpStatus.Series.SERVER_ERROR);

    public static boolean isError(final HttpStatus statusCode) {
        return ERRORS.contains(statusCode.series());
    }
}
