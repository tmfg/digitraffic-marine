package fi.livi.digitraffic.meri.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(basePackages = "fi.livi.digitraffic.meri.dao", enableDefaultTransactions = false)
public class MarineApplicationConfiguration {
    private static final Logger log = LoggerFactory.getLogger(MarineApplicationConfiguration.class);

    public static final String API_BETA_BASE_PATH = "/api/beta";

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
                                 final @Value("${marine.datasource.driver:}") String driver, // default empty if property not found
                                 final @Value("${marine.datasource.hikari.maximum-pool-size:20}") Integer maximumPoolSize) {

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        if (StringUtils.isNotBlank(driver)) {
            config.setDriverClassName(driver);
        }

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
        final ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
        bean.afterPropertiesSet();
        return bean.getObject();
    }

    @Primary
    @Bean("gitProperties")
    public GitProperties gitProperties(@Value("classpath:git.properties") final Resource resource) {
        return loadGitProperties(resource);
    }

    @Bean("commonGitProperties")
    public GitProperties commonGitProperties(@Value("classpath:common-git.properties") final Resource resource) {
        return loadGitProperties(resource);
    }

    private GitProperties loadGitProperties(final Resource gitPropertiesResource) {
        try {
            final Properties p = PropertiesLoaderUtils.loadProperties(gitPropertiesResource);
            // Remove git. prefix from properties
            for (final String name : p.stringPropertyNames()) {
                if (name.startsWith("git.")) {
                    final String value = p.remove(name).toString();
                    if (StringUtils.isNotBlank(value)) {
                        p.setProperty(name.substring(4), value);
                    } else {
                        log.warn("Empty value for git info {} at git properties {}", name, gitPropertiesResource.getFilename());
                    }
                }
            }
            return new GitProperties(p);
        } catch (final IOException e) {
            log.warn("Could not load git properties", e);
            return new GitProperties(new Properties());
        }
    }
}
