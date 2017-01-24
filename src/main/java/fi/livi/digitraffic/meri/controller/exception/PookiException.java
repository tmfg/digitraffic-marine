package fi.livi.digitraffic.meri.controller.exception;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PookiException extends IOException {
    private final Map<String, Object> properties;

    public PookiException(final String message, final Map<String,Object> properties) {
        super(message);

        this.properties = properties;
    }

    public PookiException(String message) {
        super(message);

        this.properties = new HashMap<>();
    }
}
