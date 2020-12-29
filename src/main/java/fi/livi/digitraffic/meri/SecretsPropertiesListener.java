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
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SecretsPropertiesListener implements ApplicationListener<ApplicationPreparedEvent> {

    private static final Logger log = LoggerFactory.getLogger(SecretsPropertiesListener.class);

    @Override
    public void onApplicationEvent(final ApplicationPreparedEvent applicationPreparedEvent) {
        final ConfigurableEnvironment env = applicationPreparedEvent.getApplicationContext().getEnvironment();

        final String secretName = env.getProperty("spring.aws.secretsmanager.secretName");

        if (StringUtils.isBlank(secretName)) {
            return;
        }

        try {
            final String secretString = getSecret(secretName);

            final ObjectMapper om = new ObjectMapper();
            final JavaType type = om.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);
            final HashMap<String, String> secretJson = om.readValue(secretString, type);

            final Properties props = new Properties();

            for (final Map.Entry<String, String> entry : secretJson.entrySet()) {
                props.put(entry.getKey(), entry.getValue());
            }

            env.getPropertySources().addFirst(new PropertiesPropertySource("aws.secrets.manager", props));

            log.info("Successfully read secret from Secrets Manager");
        } catch (Exception e) {
            throw new RuntimeException("Error reading secret", e);
        }
    }

    private String getSecret(final String secretName) {
        final AWSSecretsManager client =
            AWSSecretsManagerClientBuilder
                .standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();

        final GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
            .withSecretId(secretName).withVersionStage("AWSCURRENT");
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

}
