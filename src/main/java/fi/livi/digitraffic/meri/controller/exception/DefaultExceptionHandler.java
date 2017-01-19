package fi.livi.digitraffic.meri.controller.exception;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import com.google.common.collect.Iterables;

@ControllerAdvice
public class DefaultExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @ExceptionHandler({ TypeMismatchException.class })
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(final TypeMismatchException exception, final ServletWebRequest request) {

        final String parameterValue = exception.getValue().toString();
        final String requiredType = exception.getRequiredType().getSimpleName();

        log.info(String.format("Query parameter type mismatch. Uri: %s, query string: %s, required type: %s",
                               request.getRequest().getRequestURI(), request.getRequest().getQueryString(), requiredType));

        return new ResponseEntity<>(new ErrorResponse(Timestamp.from(ZonedDateTime.now().toInstant()),
                                                      HttpStatus.BAD_REQUEST.value(),
                                                      HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                                      String.format("Invalid format for parameter. Target type: %s, parameter: %s",
                                                                    requiredType, parameterValue),
                                                      request.getRequest().getRequestURI()),
                                    HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<ErrorResponse> handleConstraintViolation(final ConstraintViolationException exception, final ServletWebRequest request) {

        final List<String> errors = exception.getConstraintViolations().stream().map(v -> resolveErrorMessage(v)).collect(Collectors.toList());

        log.info(String.format("Constraint violation. Uri: %s, query string: %s, violations: %s",
                               request.getRequest().getRequestURI(), request.getRequest().getQueryString(), errors));

        return new ResponseEntity<>(new ErrorResponse(Timestamp.from(ZonedDateTime.now().toInstant()),
                                                      HttpStatus.BAD_REQUEST.value(),
                                                      HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                                      StringUtils.join(errors, ", "),
                                                      request.getRequest().getRequestURI()),
                                    HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ Exception.class })
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(final Exception exception, final ServletWebRequest request) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.value() + " " + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception);

        return new ResponseEntity<>(new ErrorResponse(Timestamp.from(ZonedDateTime.now().toInstant()),
                                                      HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                      HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                                      "Unknown error",
                                                      request.getRequest().getRequestURI()),
                                    HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static String resolveErrorMessage(final ConstraintViolation<?> violation) {
        return String.format("Violation: %s = %s - %s", Iterables.getLast(violation.getPropertyPath()), violation.getInvalidValue(), violation.getMessage());
    }
}