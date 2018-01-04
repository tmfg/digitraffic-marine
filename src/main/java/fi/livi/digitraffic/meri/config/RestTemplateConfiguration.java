package fi.livi.digitraffic.meri.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import fi.livi.digitraffic.meri.util.web.Jax2bRestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    /**
     * @return RestTemplate for classes generated from XSD
     */
    @Bean
    public Jax2bRestTemplate jax2bRestTemplate() {
        final Jax2bRestTemplate jax2bRestTemplate = new Jax2bRestTemplate(clientHttpRequestFactory());
        jax2bRestTemplate.setMessageConverters(Collections.singletonList(new Jaxb2RootElementHttpMessageConverter()));
        return jax2bRestTemplate;
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(30 * 1000);
        factory.setReadTimeout(60 * 1000);
        return factory;
    }
}