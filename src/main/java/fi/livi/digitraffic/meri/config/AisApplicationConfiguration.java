package fi.livi.digitraffic.meri.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

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

    /**
     * Initialize OracleDataSource manually because datasource property spring.datasource.type=oracle.jdbc.pool.OracleDataSource
     * is not correctly handled by spring https://github.com/spring-projects/spring-boot/issues/6027#issuecomment-221582708
     * @param properties
     * @return
     * @throws SQLException
     */
    @Bean
    public DataSource dataSource(final DataSourceProperties properties) throws SQLException {
        final PoolDataSource dataSource = PoolDataSourceFactory.getPoolDataSource();
        dataSource.setUser(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        dataSource.setURL(properties.getUrl());
        dataSource.setFastConnectionFailoverEnabled(true);
        dataSource.setMaxPoolSize(20);
        dataSource.setMinPoolSize(5);
        dataSource.setMaxIdleTime(5);
        dataSource.setValidateConnectionOnBorrow(true);
        dataSource.setMaxStatements(10);
        dataSource.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
        return dataSource;
    }
}
