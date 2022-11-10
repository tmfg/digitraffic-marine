package fi.livi.digitraffic.meri.config;

import javax.servlet.Filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    /** Support for etag and conditional HTTP-requests */
    @Bean
    @ConditionalOnProperty(value = "etags.enabled", havingValue = "true")
    public Filter ShallowEtagHeaderFilter() {
        final ShallowEtagHeaderFilter shallowEtagHeaderFilter = new ShallowEtagHeaderFilter();
        shallowEtagHeaderFilter.setWriteWeakETag(true);
        return shallowEtagHeaderFilter;
    }

    /**
     * This redirects requests from root / to /swagger-ui/index.html.
     * @param registry
     */
    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/swagger-ui/index.html");
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new DeprecationInterceptor());
        registry.addInterceptor(new AllowedParameterInterceptor());
    }
}
