package fi.livi.digitraffic.meri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import fi.livi.digitraffic.meri.annotation.CoverageIgnore;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@EntityScan(basePackages = { "fi.livi.util", "fi.livi.digitraffic.meri"})
@ComponentScan(basePackages = {"fi.livi.util", "fi.livi.digitraffic.meri"})
public class AisApplication {
    @CoverageIgnore
    public static void main(String[] args) {
        SpringApplication.run(AisApplication.class, args);
    }
}
