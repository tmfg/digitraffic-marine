package fi.livi.digitraffic.meri.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.Filter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public Filter ShallowEtagHeaderFilter() {
        final ShallowEtagHeaderFilter shallowEtagHeaderFilter = new ShallowEtagHeaderFilter();
        shallowEtagHeaderFilter.setWriteWeakETag(true);
        return shallowEtagHeaderFilter;
    }

    @Override
    public void configurePathMatch(final PathMatchConfigurer matcher) {
        // Allow dots in {from} part in request: GET /locations/mmsi/{mmsi}/radius/{radius}/from/{from}
        // Otherwise GET locations/mmsi/666/radius/100/from/2016-02-01T10:49:46.000Z would result into TypeMismatch @ {from}
        matcher.setUseSuffixPatternMatch(false);
    }

    /**
     * This redirects requests from root / to /swagger-ui/index.html.
     * @param registry
     */
    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/swagger-ui/index.html");
    }
}
