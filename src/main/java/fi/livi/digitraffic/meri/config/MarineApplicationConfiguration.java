package fi.livi.digitraffic.meri.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(basePackages = "fi.livi.digitraffic.meri.dao", enableDefaultTransactions = false)
public class MarineApplicationConfiguration {
    public static final String API_V1_BASE_PATH = "/api/v1";
    public static final String API_V2_BASE_PATH = "/api/v2";

    public static final String API_BETA_BASE_PATH = "/api/beta";
    public static final String API_METADATA_PART_PATH = "/metadata";
    public static final String API_LOCATIONS_PATH = "/locations";
    public static final String API_PORT_CALLS_PATH = "/port-calls";
    public static final String API_WINTER_NAVIGATION_PATH = "/winter-navigation";
    public static final String API_SSE_PATH = "/sse";

    /**
     * Enables bean validation for controller parameters
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    @Primary
    public DataSource dataSource(final @Value("${marine.datasource.url}") String url,
                                 final @Value("${marine.datasource.username}") String username,
                                 final @Value("${marine.datasource.password}") String password,
                                 final @Value("${marine.datasource.driver}") String driver,
                                 final @Value("${marine.datasource.hikari.maximum-pool-size:20}") Integer maximumPoolSize) {

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);

        config.setMaximumPoolSize(maximumPoolSize);

        config.setMaxLifetime(570000);
        config.setIdleTimeout(500000);
        config.setConnectionTimeout(60000);
        config.setPoolName("application_pool");

        // register mbeans for debug
        config.setRegisterMbeans(true);

        return new HikariDataSource(config);
    }

    @Bean
    // fix bug in spring boot, tries to export hikari beans twice
    public MBeanExporter exporter() {
        final MBeanExporter exporter = new MBeanExporter();

        exporter.setAutodetect(true);
        exporter.setExcludedBeans("dataSource", "quartzDataSource");

        return exporter;
    }

    @Bean("conversionService")
    public org.springframework.core.convert.ConversionService getConversionService() {
        ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
        bean.afterPropertiesSet();
        ConversionService object = bean.getObject();
        return object;
    }
}
