package fi.livi.digitraffic.meri.service.portnet.location;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import fi.livi.digitraffic.meri.model.portnet.SsnLocation;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnNotWebApplication
public class SsnLocationClient {
    private static final String FILENAME = "/meta_ssn_locodes.csv";

    private final WebClient webClient;
    private final SsnLocationReader ssnLocationReader;

    public SsnLocationClient(@Value("${metadata.csv.baseUrl:}") final String baseUrl, final WebClient webClient) {
        this.ssnLocationReader = new SsnLocationReader();
        this.webClient = webClient.mutate().baseUrl(baseUrl+FILENAME).build();
    }

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 30000))
    public List<SsnLocation> getSsnLocations() {
        final String res = webClient.get().retrieve().bodyToMono(String.class).block();

        return ssnLocationReader.readCsv(res);
    }
}
