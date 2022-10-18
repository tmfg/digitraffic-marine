package fi.livi.digitraffic.meri.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking the sunset date of a deprecated API.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD})
public @interface Sunset {
    /**
     * The earliest date at which the deprecated API may be taken down.
     *
     * @return sunset date as string in format YYYY-MM-DD
     */
    String date();
}

