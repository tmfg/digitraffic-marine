package fi.livi.digitraffic.meri.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {
    private static final String PORTNET_PRIVATE_KEY_STORE_FILENAME = "portnet.p12";
    private static final String PORTNET_PUBLIC_KEY_FILENAME = "portnet-cert.cer";

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
    @ConditionalOnExpression("'${config.test}' == 'true'")
    public RestTemplate restTemplateForTest() {
        return jax2bRestTemplate();
    }

    @Bean
    @ConditionalOnExpression("'${config.test}' != 'true'")
    public RestTemplate authenticatedRestTemplate() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException, InvalidKeySpecException {
        final KeyStore clientKeyStore = openKeyStore(PORTNET_PRIVATE_KEY_STORE_FILENAME);
        final KeyStore serverKeyStore = generateKeyStore(PORTNET_PUBLIC_KEY_FILENAME);

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

    private KeyStore openKeyStore(final String filename) throws KeyStoreException, CertificateException, NoSuchAlgorithmException,
        IOException {
        final KeyStore ks = KeyStore.getInstance("PKCS12");
        //ks.load(ClassLoader.getSystemResourceAsStream(filename), EMPTY_PASSWORD);
        ks.load(this.getClass().getClassLoader().getResourceAsStream(filename), EMPTY_PASSWORD);
        return ks;
    }

    private KeyStore generateKeyStore(final String filename) throws CertificateException, KeyStoreException,
        IOException, NoSuchAlgorithmException {
        final CertificateFactory cf = CertificateFactory.getInstance("X.509");
        //final InputStream is = ClassLoader.getSystemResourceAsStream(filename);
        final InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);
        final KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null);
        ks.setCertificateEntry("alias", cf.generateCertificate(is));

        return ks;
    }

    private ClientHttpRequestFactory clientHttpRequestFactory(final HttpClient client) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(client);
        factory.setConnectTimeout(30 * 1000);
        factory.setReadTimeout(60 * 1000);
        return factory;
    }
}