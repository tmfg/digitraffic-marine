package fi.livi.digitraffic.meri.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.util.Base64;

import javax.net.ssl.KeyManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfiguration {
    private static final Logger log = LoggerFactory.getLogger(WebClientConfiguration.class);

    public static final char[] EMPTY_PASSWORD = "".toCharArray();

    @Bean
    public WebClient webClient() {
        // more memory for default web-client
        return WebClient.builder()
            .exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                    .defaultCodecs()
                    .maxInMemorySize(10 * 1024 * 1024))
                .build())
            .build();
    }

    @Bean("portnetWebClient")
    @Profile("!aws")
    public WebClient webClientForTest() {
        log.info("Init WebClient without authentication");
        return WebClient.builder().exchangeStrategies(
            ExchangeStrategies.builder()
                .codecs(configurer ->
                    configurer.defaultCodecs().jaxb2Decoder(new Jaxb2XmlDecoder())
                )
                .build()
        ).build();
    }
    @Bean
    @Profile("aws")
    public WebClient portnetWebClient(@Value("${portnet.privatekey}") final String portnetPrivateKeyBase64) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        log.info("Init WebClient with authentication");
        final KeyStore clientKeyStore = openKeyStore(portnetPrivateKeyBase64);
        final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(clientKeyStore, EMPTY_PASSWORD);

        final SslContext sslContext = SslContextBuilder.forClient()
            .keyManager(keyManagerFactory)
            .trustManager(InsecureTrustManagerFactory.INSTANCE)
            .build();

        final HttpClient httpClient = HttpClient.create()
            .responseTimeout(Duration.ofSeconds(30))
            .secure(sslSpec -> sslSpec.sslContext(sslContext));

        return WebClient.builder()
            .exchangeStrategies(ExchangeStrategies.builder()
                .codecs(configurer -> {
                    final ClientCodecConfigurer.ClientDefaultCodecs dc = configurer.defaultCodecs();
                    // more memory for default web-client
                    dc.maxInMemorySize(10 * 1024 * 1024);
                    dc.jaxb2Decoder(new Jaxb2XmlDecoder());
                })
                .build())
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

    public static KeyStore openKeyStore(final String portnetPrivateKeyBase64) throws KeyStoreException, CertificateException, NoSuchAlgorithmException,
        IOException {
        final byte[] portnetPrivateKeyDecoded = Base64.getDecoder().decode(portnetPrivateKeyBase64);
        final KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new ByteArrayInputStream(portnetPrivateKeyDecoded), EMPTY_PASSWORD);
        return ks;
    }
}
