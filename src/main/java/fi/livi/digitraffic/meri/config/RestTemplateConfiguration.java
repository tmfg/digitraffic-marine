package fi.livi.digitraffic.meri.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Collections;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {
    private static final Logger log = LoggerFactory.getLogger(RestTemplateConfiguration.class);
    private static final char[] EMPTY_PASSWORD = "".toCharArray();

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory(HttpClients.createSystem()));
    }

    /**
     * @return RestTemplate for classes generated from XSD
     */
    @Bean
    public RestTemplate jax2bRestTemplate() {
        final RestTemplate jax2bRestTemplate = new RestTemplate(clientHttpRequestFactory(HttpClients.createSystem()));
        jax2bRestTemplate.setMessageConverters(Collections.singletonList(new Jaxb2RootElementHttpMessageConverter()));
        return jax2bRestTemplate;
    }

    @Bean("authenticatedRestTemplate")
//    @ConditionalOnExpression("'${config.test}' == 'true'")
    @Profile("!aws")
    public RestTemplate restTemplateForTest() {
        log.info("Init RestTemplate without authentication");
        return jax2bRestTemplate();
    }

    @Bean
//    @ConditionalOnExpression("'${config.test}' != 'true' && ")
    @Profile("aws")
    public RestTemplate authenticatedRestTemplate(@Value("${portnet.privatekey}") final String portnetPrivateKeyBase64) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException, InvalidKeySpecException {
        log.info("Init RestTemplate with authentication");
        final KeyStore clientKeyStore = openKeyStore(portnetPrivateKeyBase64);

        final SSLContextBuilder sslContextBuilder = new SSLContextBuilder()
            .setProtocol("TLS")
            .loadKeyMaterial(clientKeyStore, EMPTY_PASSWORD)
            .loadTrustMaterial((chain, authType) -> true);

        final SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build());

        final CloseableHttpClient httpClient = HttpClients.custom()
            .setSSLSocketFactory(sslConnectionSocketFactory)
            .build();

        return new RestTemplate(clientHttpRequestFactory(httpClient));
    }

    private KeyStore openKeyStore(final String portnetPrivateKeyBase64) throws KeyStoreException, CertificateException, NoSuchAlgorithmException,
        IOException {
        final byte[] portnetPrivateKeyDecoded = Base64.getDecoder().decode(portnetPrivateKeyBase64);
        final KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new ByteArrayInputStream(portnetPrivateKeyDecoded), EMPTY_PASSWORD);
        return ks;
    }

    private ClientHttpRequestFactory clientHttpRequestFactory(final HttpClient client) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(client);
        factory.setConnectTimeout(30 * 1000);
        factory.setReadTimeout(60 * 1000);
        return factory;
    }
}