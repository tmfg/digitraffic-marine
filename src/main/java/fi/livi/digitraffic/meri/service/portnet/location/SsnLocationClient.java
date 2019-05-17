package fi.livi.digitraffic.meri.service.portnet.location;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnNotWebApplication
public class SsnLocationClient {
    private static final String FILENAME = "/meta_ssn_locodes.csv";

    private final RestTemplate restTemplate;
    private final SsnLocationReader ssnLocationReader;

    public SsnLocationClient(@Value("${metadata.csv.baseUrl:}") final String baseUrl, final RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.rootUri(baseUrl).build();
        this.ssnLocationReader = new SsnLocationReader();
    }

    public List<SsnLocation> getSsnLocations() {
        final String res = restTemplate.getForObject(FILENAME, String.class);

        return ssnLocationReader.readCsv(res);
    }
}
