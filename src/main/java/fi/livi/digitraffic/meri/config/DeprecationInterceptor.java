package fi.livi.digitraffic.meri.config;

import static fi.livi.digitraffic.meri.util.TimeUtil.isoLocalDateToHttpDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import fi.livi.digitraffic.meri.annotation.Sunset;
import fi.livi.digitraffic.meri.controller.ApiDeprecations;

public class DeprecationInterceptor implements HandlerInterceptor {

    private final static Logger log = LoggerFactory.getLogger(DeprecationInterceptor.class);

    @Override
    public boolean preHandle(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Object handler) throws Exception {

        final HandlerMethod handlerMethod;

        try {
            handlerMethod = (HandlerMethod) handler;

            if (handlerMethod.getMethod().isAnnotationPresent(Deprecated.class)
                || handlerMethod.getMethod().isAnnotationPresent(Sunset.class)) {

                if (handlerMethod.getMethod().isAnnotationPresent(Deprecated.class)
                    && handlerMethod.getMethod().isAnnotationPresent(Sunset.class)) {

                    final String sunsetHeaderContent = handlerMethod.getMethod().getAnnotation(Sunset.class).tbd() ?
                                                       ApiDeprecations.SUNSET_FUTURE :
                                                       isoLocalDateToHttpDateTime(handlerMethod.getMethod().getAnnotation(Sunset.class).date());

                    response.addHeader("Deprecation", "true");
                    response.addHeader("Sunset", sunsetHeaderContent);

                } else {
                    throw new Exception(
                        "Deprecated handler " + handlerMethod.getMethod().getName() + " is missing either a @Deprecated or @Sunset annotation");
                }
            }
        } catch (final Exception error) {
            log.error(error.getMessage());
        }
        return true;
    }

}
