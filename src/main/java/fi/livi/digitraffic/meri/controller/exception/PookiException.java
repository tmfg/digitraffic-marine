package fi.livi.digitraffic.meri.controller.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class PookiException extends IOException {

    public PookiException(final String message, final Exception e) {
        super(message, e);
    }
}
