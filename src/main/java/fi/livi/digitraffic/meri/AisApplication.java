package fi.livi.digitraffic.meri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AisApplication {

	public static void main(String[] args) {
		SpringApplication.run(AisApplication.class, args);
	}
}
