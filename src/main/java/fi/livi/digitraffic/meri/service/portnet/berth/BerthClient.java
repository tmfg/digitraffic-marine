package fi.livi.digitraffic.meri.service.portnet.berth;

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

    public BerthClient(@Value("${metadata.csv.baseUrl:}") final String baseUrl, final WebClient webClient) {
        this.webClient = webClient.mutate().baseUrl(baseUrl + FILENAME).build();
        this.berthReader = new BerthReader();
    }

    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 30000))
    public List<BerthLine> getBerthLines() {
        final String res = webClient.get().retrieve().bodyToMono(String.class).block();

        return berthReader.readCsv(res);
    }

}
