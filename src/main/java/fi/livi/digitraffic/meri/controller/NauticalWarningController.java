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

    private String pookiUrl;

    public enum Status {
        DRAFT("merivaroitus_luonnos_dt"),
        PUBLISHED("merivaroitus_julkaistu_dt"),
        ARCHIVED("merivaroitus_arkistoitu_dt");

        public final String layer;

        Status(final String layer){
            this.layer = layer;
        }
    }

    @Autowired
    public NauticalWarningController(@Value("${ais.pooki.url}") final String pookiUrl) {
        this.pookiUrl = pookiUrl;
        template = new RestTemplate();
        template.setErrorHandler(new NauticalWarningErrorHandler());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/nautical-warnings/{status}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<?> nauticalWarnings(@PathVariable final String status) {
        final Status s = Status.valueOf(status.toUpperCase());
        final String url = String.format("%s?layer=%s", pookiUrl, s.layer);

        ResponseEntity<String> response = template.getForEntity(url, String.class);

        if (RestUtil.isError(response.getStatusCode())) {
            response = template.getForEntity(url, String.class);
        }

        if (RestUtil.isError(response.getStatusCode())) {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

        return ResponseEntity.ok().body(response.getBody());
    }

    public void setPookiUrl(final String pookiUrl) {
        this.pookiUrl = pookiUrl;
    }
}
