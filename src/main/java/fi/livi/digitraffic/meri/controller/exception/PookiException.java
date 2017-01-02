package fi.livi.digitraffic.meri.controller.exception;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PookiException extends IOException {

    private Map<String, Object> properties;

    public PookiException(String message) {
        super(message);
    }

    public void setProperties(Map<String,Object> properties) {
        this.properties = properties;
    }
}
