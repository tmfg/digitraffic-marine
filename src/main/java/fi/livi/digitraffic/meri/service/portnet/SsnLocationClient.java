package fi.livi.digitraffic.meri.service.portnet;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;

@Component
public class SsnLocationClient {
    private final String baseUrl;

    private static final String FILENAME = "meta_ssn_locodes.csv";

    private final RestTemplate restTemplate = new RestTemplate();

    private final SsnLocationReader ssnLocationReader = new SsnLocationReader();

    public SsnLocationClient(@Value("${metadata.csv.baseUrl}") final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<SsnLocation> getSsnLocations() {
        final String res = restTemplate.getForObject(baseUrl + FILENAME, String.class);

        return ssnLocationReader.readCsv(res);
    }
}
