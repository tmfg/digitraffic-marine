package fi.livi.digitraffic.meri.service.portnet.berth;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BerthClient {
    private final String baseUrl;

    private static final String FILENAME= "meta_berths.csv";

    private final RestTemplate restTemplate = new RestTemplate();

    private final BerthReader reader = new BerthReader();

    public BerthClient(@Value("${metadata.csv.baseUrl}") final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<BerthLine> getBerthLines() throws IOException {
        final String res = restTemplate.getForObject(baseUrl + FILENAME, String.class);

        return reader.readCsv(res);
    }

}
