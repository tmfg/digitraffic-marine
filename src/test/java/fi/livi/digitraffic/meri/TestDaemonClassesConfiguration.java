package fi.livi.digitraffic.meri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fi.livi.digitraffic.meri.dao.portnet.SsnLocationRepository;
import fi.livi.digitraffic.meri.model.sse.SseFeature;
import fi.livi.digitraffic.meri.service.portnet.location.LocationCoordinateReader;
import fi.livi.digitraffic.meri.service.portnet.location.SsnLocationClient;
import fi.livi.digitraffic.meri.service.portnet.location.SsnLocationUpdater;
import fi.livi.digitraffic.meri.service.sse.SseDataListener;

/**
 * Definitons for beans to satisfy test dependencies
 */
@Configuration
public class TestDaemonClassesConfiguration {
    private static final Logger log = LoggerFactory.getLogger(TestDaemonClassesConfiguration.class);

    @Bean
    public SsnLocationClient ssnLocationClient(@Value("${metadata.csv.baseUrl:}") final String baseUrl, final RestTemplateBuilder restTemplateBuilder) {
        return new SsnLocationClient(baseUrl, restTemplateBuilder);
    }

    @Bean
    public SsnLocationUpdater ssnLocastionUpdater(final SsnLocationRepository ssnLocationRepository, final SsnLocationClient
        ssnLocationClient, final LocationCoordinateReader locationCoordinateReader) {
        return new SsnLocationUpdater(ssnLocationRepository, ssnLocationClient, locationCoordinateReader);
    }

    @Bean
    public SseDataListener sseDataListener() {
        return new SseDataListener(null, null) {
            @Override
            public void receiveMessage(SseFeature message) {
                log.info("SseDataListener.receiveMessage: {}", message);
            }
        };
    }
}
