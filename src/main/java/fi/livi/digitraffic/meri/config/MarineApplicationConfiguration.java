package fi.livi.digitraffic.meri.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(basePackages = "fi.livi.digitraffic.meri.dao")
public class MarineApplicationConfiguration {
    public static final String API_V1_BASE_PATH = "/api/v1";
    public static final String API_BETA_BASE_PATH = "/api/beta";
    public static final String API_METADATA_PART_PATH = "/metadata";
    public static final String API_LOCATIONS_PATH = "/locations";
    public static final String API_PORT_CALLS_PATH = "/port-calls";
    public static final String API_WINTER_NAVIGATION_PATH = "/winter-navigation";

    /**
     * Enables bean validation for controller parameters
     * @return
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @SuppressWarnings("Duplicates")
    @Bean
    public DataSource datasource(final @Value("${marine.datasource.url}") String url,
                                 final @Value("${marine.datasource.username}") String username,
                                 final @Value("${marine.datasource.password}") String password,
                                 final @Value("${marine.datasource.hikari.maximum-pool-size:20}") Integer maximumPoolSize) {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

        config.setMaximumPoolSize(maximumPoolSize);

        config.setMaxLifetime(570000);
        config.setIdleTimeout(500000);
        config.setConnectionTimeout(60000);

        // Auto commit must be true for Quartz
        config.setAutoCommit(true);

        return new HikariDataSource(config);
    }
}
