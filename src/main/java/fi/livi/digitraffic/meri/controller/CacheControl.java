package fi.livi.digitraffic.meri.controller;

import jakarta.servlet.http.HttpServletResponse;

public class CacheControl {
    public static final int CACHE_AGE_MINUTE = 60;
    public static final int CACHE_AGE_HOUR = 60 * CACHE_AGE_MINUTE;
    public static final int CACHE_AGE_DAY = 24 * CACHE_AGE_HOUR;

    public static void setCacheMaxAgeSeconds(final HttpServletResponse response, final int maxAge) {
        response.setHeader("Cache-Control", String.format("max-age=%d, public", maxAge));
    }

    public static void setOneDayCache(final HttpServletResponse response) {
        setCacheMaxAgeSeconds(response, CACHE_AGE_DAY);
    }

    public static void setOneHourCache(final HttpServletResponse response) {
        setCacheMaxAgeSeconds(response, CACHE_AGE_HOUR);
    }

    public static void setOneMinuteCache(final HttpServletResponse response) {
        setCacheMaxAgeSeconds(response, CACHE_AGE_MINUTE);
    }
}
