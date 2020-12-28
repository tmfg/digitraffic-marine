package fi.livi.digitraffic.meri;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SecretsPropertiesListener implements ApplicationListener<ApplicationPreparedEvent> {

    private static final ObjectMapper om = new ObjectMapper();

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent applicationPreparedEvent) {
        final ConfigurableEnvironment env = applicationPreparedEvent.getApplicationContext().getEnvironment();
        try {
            final Properties props = new Properties();
            final JavaType type = om.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);
            final HashMap<String, String> secretJson = om.readValue(getSecret(), type);
            for (final Map.Entry<String, String> entry : secretJson.entrySet()) {
                props.put(entry.getKey(), entry.getValue());
            }
            env.getPropertySources().addFirst(new PropertiesPropertySource("aws.secrets.manager", props));
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing or writing secret", e);
        }
    }

    private String getSecret() {
        AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard();
        AWSSecretsManager client = clientBuilder.build();

        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
            .withSecretId("marine").withVersionStage("AWSCURRENT");
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
