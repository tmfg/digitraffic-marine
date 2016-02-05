package fi.livi.digitraffic.meri.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"fi.livi.digitraffic.meri.dao"})
public class AisApplicationConfiguration {
    public static final String API_V1_BASE_PATH = "/api/v1";
    public static final String API_WEBSOCKETS_PART_PATH = "/websockets";
    public static final String API_METADATA_PART_PATH = "/metadata";
}