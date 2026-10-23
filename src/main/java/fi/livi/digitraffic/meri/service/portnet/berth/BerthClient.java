package fi.livi.digitraffic.meri.service.portnet.berth;

import fi.livi.digitraffic.common.logging.HttpLogger;
import fi.livi.digitraffic.common.logging.HttpLoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@ConditionalOnNotWebApplication
public class BerthClient {
    private static final String FILENAME= "/meta_berths.csv";

    private final WebClient webClient;
    private final BerthReader berthReader;

    private final String url;
    private final HttpLoggerFactory factory;

    public BerthClient(@Value("${metadata.csv.baseUrl:}") final String baseUrl, final WebClient webClient) {
        this.url = baseUrl + FILENAME;
        this.factory = new HttpLoggerFactory("Port-call", null, url);
        this.webClient = webClient.mutate().baseUrl(url).build();
        this.berthReader = new BerthReader();
    }

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 30000))
    public List<BerthLine> getBerthLines() {
        final HttpLogger httpLogger = this.factory.logger(url);

        final String res = webClient.get()
            .exchangeToMono(response -> httpLogger.log(response, String.class))
            .block();

        return berthReader.readCsv(res);
    }

}
