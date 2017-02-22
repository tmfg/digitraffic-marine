package fi.livi.digitraffic.meri.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void configurePathMatch(final PathMatchConfigurer matcher) {
        // Allow dots in {from} part in request: GET /locations/mmsi/{mmsi}/radius/{radius}/from/{from}
        // Otherwise GET locations/mmsi/666/radius/100/from/2016-02-01T10:49:46.000Z would result into TypeMismatch @ {from}
        matcher.setUseSuffixPatternMatch(false);
    }
}
