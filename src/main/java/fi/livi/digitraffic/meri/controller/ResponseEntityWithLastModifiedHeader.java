package fi.livi.digitraffic.meri.controller;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Used as controller method return value to add LastModified header to responses.
 * Usefull when there is no root object that contains last modified information i.e. with plain array of data
 * and @{{@link fi.livi.digitraffic.meri.config.LastModifiedAppenderControllerAdvice} cannont be used.
 */
public class ResponseEntityWithLastModifiedHeader<T> extends ResponseEntity<T> {
    private static final Logger log = LoggerFactory.getLogger(ResponseEntityWithLastModifiedHeader.class);

    private ResponseEntityWithLastModifiedHeader(final T object, final Instant lastModified) {
        super(object, createHeaders(lastModified), HttpStatus.OK);
    }

    private static HttpHeaders createHeaders(final Instant lastModified) {
        if (lastModified != null) {
            final HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setLastModified(lastModified);
            return responseHeaders;
        }
        return null;
    }

    public static <T> ResponseEntityWithLastModifiedHeader<T> of(final T object, final Instant lastModified) {
        if (lastModified == null) {
            log.error("ResponseEntityWithLastModifiedHeader created with null lastModified value for " + object.getClass().getSimpleName());
        }
        return new ResponseEntityWithLastModifiedHeader<>(object, lastModified);
    }
}