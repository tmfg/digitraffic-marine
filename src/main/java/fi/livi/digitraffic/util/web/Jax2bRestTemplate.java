package fi.livi.digitraffic.util.web;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class Jax2bRestTemplate extends RestTemplate {

    public Jax2bRestTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        super(clientHttpRequestFactory);
    }
}
