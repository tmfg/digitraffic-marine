package fi.livi.digitraffic.meri;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
@ConditionalOnProperty("spring.aws.secretsmanager.secretName")
public class SecretsPropertiesListener implements ApplicationListener<ApplicationPreparedEvent> {

    private static final Logger log = LoggerFactory.getLogger(SecretsPropertiesListener.class);

    @Value("${spring.aws.secretsmanager.secretName}")
    private String secretName;

    @Override
    public void onApplicationEvent(final ApplicationPreparedEvent applicationPreparedEvent) {
        log.info("About to read secrets from Secrets Manager");

        final ConfigurableEnvironment env = applicationPreparedEvent.getApplicationContext().getEnvironment();

        try {
            final String secretString = getSecret(secretName);
            final HashMap<String, String> secretJson = parseSecret(secretString);
            env.getPropertySources().addFirst(new PropertiesPropertySource("aws.secrets.manager", secretsToProps(secretJson)));

            log.info("Successfully read secret from Secrets Manager");
        } catch (Exception e) {
            throw new RuntimeException("Error reading secret from Secrets Manager", e);
        }
    }

    private String getSecret(final String secretName) {
        final AWSSecretsManager client =
            AWSSecretsManagerClientBuilder
                .standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();

        final GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
            .withSecretId(secretName).withVersionStage("AWSCURRENT"); // newest version of Secret
        try {
            final GetSecretValueResult getSecretValueResult = client.getSecretValue(getSecretValueRequest);

            if (getSecretValueResult == null) {
                throw new IllegalArgumentException("Value of secret was null");
            }

            return getSecretValueResult.getSecretString();

        } catch (Exception e) {
            throw new RuntimeException("Error getting secret", e);
        }
    }

    private HashMap<String, String> parseSecret(final String secretString) throws Exception {
        final ObjectMapper om = new ObjectMapper();
        final JavaType type = om.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);
        return om.readValue(secretString, type);
    }

    private Properties secretsToProps(final HashMap<String, String> secretJson) {
        final Properties props = new Properties();

        for (final Map.Entry<String, String> entry : secretJson.entrySet()) {
            props.put(entry.getKey(), entry.getValue());
        }

        return props;
    }
}
