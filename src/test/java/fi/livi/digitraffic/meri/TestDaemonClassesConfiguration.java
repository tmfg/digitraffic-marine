package fi.livi.digitraffic.meri;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fi.livi.digitraffic.meri.dao.portnet.SsnLocationRepository;
import fi.livi.digitraffic.meri.service.portnet.location.LocationCoordinateReader;
import fi.livi.digitraffic.meri.service.portnet.location.SsnLocationClient;
import fi.livi.digitraffic.meri.service.portnet.location.SsnLocationUpdater;

@Configuration
public class TestDaemonClassesConfiguration {
    @Bean
    public SsnLocationClient ssnLocationClient(@Value("${metadata.csv.baseUrl:}") final String baseUrl, final RestTemplateBuilder restTemplateBuilder) {
        return new SsnLocationClient(baseUrl, restTemplateBuilder);
    }

    @Bean
    public SsnLocationUpdater ssnLocastionUpdater(final SsnLocationRepository ssnLocationRepository, final SsnLocationClient
        ssnLocationClient, final LocationCoordinateReader locationCoordinateReader) {
        return new SsnLocationUpdater(ssnLocationRepository, ssnLocationClient, locationCoordinateReader);
    }
}
