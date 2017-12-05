package fi.livi.digitraffic.meri.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
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

    @SuppressWarnings("NonConstantFieldWithUpperCaseName")
    @Value("${ci.datasource.initialPoolSize:20}")
    private Integer INITIAL_POOL_SIZE;
    @Value("${ci.datasource.maxPoolSize:20}")
    private Integer MAX_POOL_SIZE;
    @Value("${ci.datasource.minPoolSize:20}")
    private Integer MIN_POOL_SIZE;

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
    @SuppressWarnings("Duplicates")
    @Bean
    public DataSource dataSource(final DataSourceProperties properties) throws SQLException {
        final PoolDataSource dataSource = PoolDataSourceFactory.getPoolDataSource();
        dataSource.setUser(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        dataSource.setURL(properties.getUrl());
        dataSource.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
        final String DPO_365_SOCKET_READ_TIMEOUT_FIX = "50000";
        dataSource.setConnectionProperty("oracle.jdbc.ReadTimeout", DPO_365_SOCKET_READ_TIMEOUT_FIX);
        dataSource.setFastConnectionFailoverEnabled(true);
        dataSource.setValidateConnectionOnBorrow(true);
        dataSource.setSQLForValidateConnection("select 1 from dual"); // DPO-365 try to fix "IO Error: Socket closed" errors.
        dataSource.setInitialPoolSize(INITIAL_POOL_SIZE);
        dataSource.setMaxPoolSize(MAX_POOL_SIZE);
        dataSource.setMinPoolSize(MIN_POOL_SIZE);
        dataSource.setMaxStatements(25);
        dataSource.setAbandonedConnectionTimeout(60);
        dataSource.setTimeToLiveConnectionTimeout(480);
        return dataSource;
    }
}
