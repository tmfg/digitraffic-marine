package fi.livi.digitraffic.meri.service.winternavigation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import fi.livi.digitraffic.meri.service.winternavigation.dto.PortsDto;
import fi.livi.digitraffic.meri.service.winternavigation.dto.ShipsDto;

@Service
public class WinterNavigationClient {

    private final String winterNavigationUrl;

    private RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(WinterNavigationClient.class);

    @Autowired
    public WinterNavigationClient(@Value("${winter.navigation.url}") final String winterNavigationUrl,
                                  final RestTemplate restTemplate) {
        this.winterNavigationUrl = winterNavigationUrl;
        this.restTemplate = restTemplate;
    }

    public PortsDto getWinterNavigationPorts() {

        return restTemplate.getForObject(winterNavigationUrl, PortsDto.class);
    }

    public ShipsDto getWinterNavigationShips() {

        return restTemplate.getForObject(winterNavigationUrl + "/ships", ShipsDto.class);
    }
}
