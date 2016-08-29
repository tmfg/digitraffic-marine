package fi.livi.digitraffic.meri.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import oracle.jdbc.pool.OracleDataSource;

@Configuration
//@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"fi.livi.digitraffic.meri.dao", "fi.livi.util"})
public class AisApplicationConfiguration {
    public static final String API_V1_BASE_PATH = "/api/v1";
    public static final String API_WEBSOCKETS_PART_PATH = "/websockets";
    public static final String API_METADATA_PART_PATH = "/metadata";

    /**
     * Initialize OracleDataSource manually because datasource property spring.datasource.type=oracle.jdbc.pool.OracleDataSource
     * is not correctly handled by spring https://github.com/spring-projects/spring-boot/issues/6027#issuecomment-221582708
     * @param properties
     * @return
     * @throws SQLException
     */
    @Bean
    public DataSource dataSource(DataSourceProperties properties) throws SQLException {
        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setUser(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        dataSource.setURL(properties.getUrl());
//        dataSource.setImplicitCachingEnabled(true);
//        dataSource.setFastConnectionFailoverEnabled(true);
        return dataSource;
    }
}