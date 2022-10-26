package fi.livi.digitraffic.meri.controller;

import org.springframework.http.MediaType;

public class MediaTypes {

    // Mobile applications lack behind in support and needs UTF-8 charset in mediatype header.
    private static final String UTF_8_VALUE_SUFFIX = ";charset=UTF-8";
    public static final String MEDIA_TYPE_APPLICATION_VND_GEO_JSON = "application/vnd.geo+json" + UTF_8_VALUE_SUFFIX ;
    public static final String MEDIA_TYPE_APPLICATION_GEO_JSON = "application/geo+json" + UTF_8_VALUE_SUFFIX ;
    public static final String MEDIA_TYPE_APPLICATION_JSON = MediaType.APPLICATION_JSON_VALUE + UTF_8_VALUE_SUFFIX;

    private MediaTypes() {}
}
