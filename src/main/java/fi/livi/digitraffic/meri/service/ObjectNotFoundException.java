package fi.livi.digitraffic.meri.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(final Class<?> entityClass, final Object identifier) {
        this(entityClass.getSimpleName(), identifier);
    }

    public ObjectNotFoundException(final String objectName, final Object identifier) {
        super("Object of [" + objectName + "] with identifier [" + identifier + "]: not found");
    }
}
