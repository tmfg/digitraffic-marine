package fi.livi.digitraffic.meri;

import fi.livi.digitraffic.meri.dao.portnet.SsnLocationRepository;
import fi.livi.digitraffic.meri.service.portnet.location.LocationCoordinateReader;
import fi.livi.digitraffic.meri.service.portnet.location.SsnLocationClient;
import fi.livi.digitraffic.meri.service.portnet.location.SsnLocationUpdater;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Definitions for beans to satisfy test dependencies
 */
@Configuration
public class TestDaemonClassesConfiguration {
    @Bean
    public SsnLocationClient ssnLocationClient(@Value("${metadata.csv.baseUrl:}") final String baseUrl, final WebClient webClient) {
        return new SsnLocationClient(baseUrl, webClient);
    }

    @Bean
    public SsnLocationUpdater ssnLocastionUpdater(final SsnLocationRepository ssnLocationRepository, final SsnLocationClient
        ssnLocationClient, final LocationCoordinateReader locationCoordinateReader) {
        return new SsnLocationUpdater(ssnLocationRepository, ssnLocationClient, locationCoordinateReader);
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

}
