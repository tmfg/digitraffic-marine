package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import fi.livi.digitraffic.util.RestUtil;

@RestController
@RequestMapping(API_V1_BASE_PATH)
public class NauticalWarningController {
    private final RestTemplate template;

    private String POOKI_URL;
    public void setPOOKI_URL(String POOKI_URL) {
        this.POOKI_URL = POOKI_URL;
    }

    @Autowired
    public NauticalWarningController(@Value("${ais.pooki.url}") final String pookiUrl) {
        POOKI_URL = pookiUrl;
        template = new RestTemplate();
        template.setErrorHandler(new NauticalWarningErrorHandler());
    }

    public enum Status {
        DRAFT("merivaroitus_luonnos_dt"),
        PUBLISHED("merivaroitus_julkaistu_dt"),
        ARCHIVED("merivaroitus_arkistoitu_dt");
        private String layer;
        Status(String layer){
            this.layer = layer;
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/nautical-warnings/{status}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity nauticalWarnings(@PathVariable String status) {

        Status s = Status.valueOf(status.toUpperCase());

        String url = String.format("%s?layer=%s", POOKI_URL, s.layer);
        ResponseEntity<String> response = template.getForEntity(url, String.class);

        if (RestUtil.isError(response.getStatusCode())) {
            response = template.getForEntity(url, String.class);
        }

        if (RestUtil.isError(response.getStatusCode())) {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

        return ResponseEntity.ok().body(response.getBody());
    }

}
