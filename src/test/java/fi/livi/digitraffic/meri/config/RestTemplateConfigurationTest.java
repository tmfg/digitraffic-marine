package fi.livi.digitraffic.meri.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

public class RestTemplateConfigurationTest {
    private final RestTemplateConfiguration restTemplateConfiguration = new RestTemplateConfiguration();

    @Test
    public void nullKey() {
        Assertions.assertThrows(NullPointerException.class, () ->
            restTemplateConfiguration.authenticatedRestTemplate(null)
        );
    }

    @Test
    public void emptyKey() {
        Assertions.assertThrows(IOException.class, () ->
            restTemplateConfiguration.authenticatedRestTemplate("")
        );
    }

    @Test
    public void invalidKey() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            restTemplateConfiguration.authenticatedRestTemplate("not_valid")
        );
    }

    @Test
    @Disabled
    public void validKey() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, InvalidKeySpecException, IOException {
        final String validKey = "test-with-your-own-key";
        final RestTemplate template = restTemplateConfiguration.authenticatedRestTemplate(validKey);
        Assertions.assertNotNull(template);
    }
}
