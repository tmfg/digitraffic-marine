package fi.livi.digitraffic.meri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import fi.livi.digitraffic.meri.annotation.CoverageIgnore;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class MarineApplication {
    @CoverageIgnore
    public static void main(final String[] args) {
        SpringApplication.run(MarineApplication.class, args);
    }
}
