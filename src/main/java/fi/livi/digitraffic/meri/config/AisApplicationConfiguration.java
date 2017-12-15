package fi.livi.digitraffic.meri.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@Configuration
@EnableWebSocketMessageBroker
@EnableJpaRepositories(basePackages = {"fi.livi.digitraffic.meri.dao", "fi.livi.util"})
public class AisApplicationConfiguration {
    public static final String API_V1_BASE_PATH = "/api/v1";
    public static final String API_BETA_BASE_PATH = "/api/beta";
    public static final String API_WEBSOCKETS_PART_PATH = "/websockets";
    public static final String API_PLAIN_WEBSOCKETS_PART_PATH = "/plain-websockets";
    public static final String API_METADATA_PART_PATH = "/metadata";
    public static final String API_LOCATIONS_PATH = "/locations";
    public static final String API_PORT_CALLS_PATH = "/port-calls";

    /**
     * Enables bean validation for controller parameters
     * @return
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

}
