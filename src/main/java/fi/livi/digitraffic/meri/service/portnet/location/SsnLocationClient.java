package fi.livi.digitraffic.meri.service.portnet.location;

import java.util.List;

import fi.livi.digitraffic.common.logging.HttpLogger;
import fi.livi.digitraffic.common.logging.HttpLoggerFactory;
import fi.livi.digitraffic.meri.portnet.xsd.VesselList;
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

    private final String url;
    private final HttpLoggerFactory factory;

    public SsnLocationClient(@Value("${metadata.csv.baseUrl:}") final String baseUrl, final WebClient webClient) {
        this.ssnLocationReader = new SsnLocationReader();
        this.url = baseUrl+FILENAME;
        this.webClient = webClient.mutate().baseUrl(url).build();

        this.factory = new HttpLoggerFactory("Port-call", null, url);
    }

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 30000))
    public List<SsnLocation> getSsnLocations() {
        final HttpLogger httpLogger = this.factory.logger(url);

        final String res = webClient.get()
            .exchangeToMono(response -> httpLogger.log(response, String.class))
            .block();

        return ssnLocationReader.readCsv(res);
    }
}
