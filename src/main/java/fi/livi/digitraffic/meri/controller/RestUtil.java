package fi.livi.digitraffic.meri.controller;

import org.springframework.http.HttpStatus;

public class RestUtil {
    public static boolean isError(HttpStatus statusCode) {
        HttpStatus.Series series = statusCode.series();
        return HttpStatus.Series.CLIENT_ERROR.equals(series)
                || HttpStatus.Series.SERVER_ERROR.equals(series);
    }
}
