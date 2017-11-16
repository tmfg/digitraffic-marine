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
        dataSource.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
        /* Starting from Oracle Database 11g Release 2 (11.2), this feature has been deprecated
         * https://docs.oracle.com/cd/E11882_01/java.112/e16548/fstconfo.htm#JJDBC26000
         * Here we use it for unknown reason. */
        dataSource.setFastConnectionFailoverEnabled(true);
        // https://docs.oracle.com/cd/E18283_01/java.112/e12265/connect.htm#CHDIDJGH
        dataSource.setValidateConnectionOnBorrow(true);

        /* ****************************************************************************************************
         * Settings below based on:
         * https://docs.oracle.com/cd/B28359_01/java.111/e10788/optimize.htm#CHDEHFHE
         */
        /* https://review.solita.fi/cru/CR-4219#c52811
         * initial max ja min kaikki samaan arvoon. Yhteyden avaus on raskas operaatio, mitä halutaan välttää. Kuudennen rinnakkaisen yhteyden
         * tarvitsemishetkellää kanta on todennäköisesti kuormitettuna ja haluamme välttää yhteyden avaamisesta aiheutuvaa ylimääräistä kuormaa.
         */
        dataSource.setInitialPoolSize(20);
        dataSource.setMaxPoolSize(20);
        dataSource.setMinPoolSize(20);
        /*
         * See:
         * https://docs.oracle.com/cd/B28359_01/java.111/e10788/optimize.htm#CFHEDJDC
         *
         * The cache size should be set to the number of distinct statements the application issues to the database.
         */
        dataSource.setMaxStatements(10);
        /* The abandoned connection timeout enables borrowed connections to be reclaimed back into the connection pool after a connection
           has not been used for a specific amount of time. Abandonment is determined by monitoring calls to the database. */
        dataSource.setAbandonedConnectionTimeout(60);
        /* The time-to-live connection timeout enables borrowed connections to remain borrowed for a specific amount of time before the
           connection is reclaimed by the pool. This timeout feature helps maximize connection reuse and helps conserve systems resources
           that are otherwise lost on maintaining connections longer than their expected usage. */
        dataSource.setTimeToLiveConnectionTimeout(480);
        /* **************************************************************************************************** */

        return dataSource;
    }
}
