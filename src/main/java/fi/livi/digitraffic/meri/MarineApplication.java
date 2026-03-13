package fi.livi.digitraffic.meri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.audit.AuditEventsEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.beans.BeansEndpointAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableRetry
@ComponentScan(basePackages = { "fi.livi.digitraffic.meri", "fi.livi.digitraffic.common"})
@SpringBootApplication(exclude = {
    AuditEventsEndpointAutoConfiguration.class,
    BeansEndpointAutoConfiguration.class
})
@EnableTransactionManagement
public class MarineApplication {
    public static void main(final String[] args) {
        SpringApplication.run(MarineApplication.class, args);
    }
}
