package fi.livi.digitraffic.meri.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import fi.livi.digitraffic.meri.annotation.Sunset;
import fi.livi.digitraffic.meri.controller.ApiDeprecations;

public class DeprecationInterceptor implements HandlerInterceptor {

    private final static Logger log = LoggerFactory.getLogger(DeprecationInterceptor.class);

    @Override
    public void postHandle(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Object handler,
        final ModelAndView modelAndView) throws Exception {

        final HandlerMethod handlerMethod;

        try {
            handlerMethod = (HandlerMethod) handler;

            if (handlerMethod.getMethod().isAnnotationPresent(Deprecated.class)
                || handlerMethod.getMethod().isAnnotationPresent(Sunset.class)) {

                if (handlerMethod.getMethod().isAnnotationPresent(Deprecated.class)
                    && handlerMethod.getMethod().isAnnotationPresent(Sunset.class)) {

                    final String sunsetDate = handlerMethod.getMethod().getAnnotation(Sunset.class).date();
                    final String sunsetHeaderContent = sunsetDate.equals(ApiDeprecations.SUNSET_FUTURE) ?
                                                       sunsetDate :
                                                       isoToHttpDate(handlerMethod.getMethod().getAnnotation(Sunset.class).date());

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

    }

    public static String isoToHttpDate(String yearMonthDayIso) throws ParseException {
        SimpleDateFormat yearMonthDayFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat httpDate = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        httpDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        return httpDate.format(yearMonthDayFormat.parse(yearMonthDayIso));
    }

}
