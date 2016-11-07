package fi.livi.digitraffic.meri.service.portnet;

import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class SsnLocationClient {

    private static final Logger log = LoggerFactory.getLogger(SsnLocationClient.class);

    @Value("${metadata.csv.baseUrl}")
    private String baseUrl;

    private final String filename = "meta_ssn_locodes.csv";

    private final RestTemplate restTemplate = new RestTemplate();

    private final SsnLocationReader ssnLocationReader = new SsnLocationReader();

    public List<SsnLocation> getSsnLocations() {

        String res = restTemplate.getForObject(baseUrl + filename, String.class);

        return ssnLocationReader.readCsv(res);
    }
}
