package fi.livi.digitraffic.meri.service.portnet.berth;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@ConditionalOnNotWebApplication
public class BerthClient {
    private static final String FILENAME= "/meta_berths.csv";

    private final RestTemplate restTemplate;
    private final BerthReader berthReader;

    public BerthClient(@Value("${metadata.csv.baseUrl:}") final String baseUrl, final RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.rootUri(baseUrl).build();
        this.berthReader = new BerthReader();
    }

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 30000))
    public List<BerthLine> getBerthLines() {
        final String res = restTemplate.getForObject(FILENAME, String.class);

        return berthReader.readCsv(res);
    }

}
