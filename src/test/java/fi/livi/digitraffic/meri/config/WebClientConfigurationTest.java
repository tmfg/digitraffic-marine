package fi.livi.digitraffic.meri.config;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

public class WebClientConfigurationTest {
    private final WebClientConfiguration webClientConfiguration = new WebClientConfiguration();

    private final WebApplicationContextRunner webContextRunner = new WebApplicationContextRunner()
        .withUserConfiguration(WebClientConfiguration.class)
        .withPropertyValues("spring.profiles.active=aws");

    private final ApplicationContextRunner nonWebContextRunner = new ApplicationContextRunner()
        .withUserConfiguration(WebClientConfiguration.class)
        .withPropertyValues("spring.profiles.active=aws");

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

    @Test
    public void awsWebContextDoesNotRequirePortnetPrivateKey() {
        webContextRunner.run(context -> {
            Assertions.assertTrue(context.isRunning());
            Assertions.assertFalse(context.containsBean("portnetWebClient"));
        });
    }

    @Test
    public void awsNonWebContextCreatesAuthenticatedPortnetWebClient() {
        nonWebContextRunner
            .withPropertyValues("portnet.privatekey=not_valid")
            .run(context -> {
                final Throwable startupFailure = context.getStartupFailure();
                Assertions.assertNotNull(startupFailure);
                Assertions.assertInstanceOf(IllegalArgumentException.class, getRootCause(startupFailure));
            });
    }

    private static Throwable getRootCause(final Throwable throwable) {
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }
}
