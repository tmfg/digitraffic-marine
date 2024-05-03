package fi.livi.digitraffic.meri.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

public class WebClientConfigurationTest {
    private final WebClientConfiguration webClientConfiguration = new WebClientConfiguration();

    @Test
    public void nullKey() {
        Assertions.assertThrows(NullPointerException.class, () ->
            webClientConfiguration.portnetWebClient(null)
        );
    }

    @Test
    public void emptyKey() {
        Assertions.assertThrows(IOException.class, () ->
            webClientConfiguration.portnetWebClient("")
        );
    }

    @Test
    public void invalidKey() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            webClientConfiguration.portnetWebClient("not_valid")
        );
    }
}
