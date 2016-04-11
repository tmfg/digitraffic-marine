package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(API_V1_BASE_PATH)
public class NauticalWarningController {

    private RestTemplate template;

    private String POOKI_URL;
    public void setPOOKI_URL(String POOKI_URL) {
        this.POOKI_URL = POOKI_URL;
    }

    @Autowired
    public NauticalWarningController(@Value("${ais.pooki.url}") final String pooki_url) {
        POOKI_URL = pooki_url;
        template = new RestTemplate();
        template.setErrorHandler(new NauticalWarningErrorHandler());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/nautical-warnings",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity nauticalWarnings() {

        ResponseEntity<String> response = template.getForEntity(POOKI_URL, String.class);

        if (RestUtil.isError(response.getStatusCode())) {
            response = template.getForEntity(POOKI_URL, String.class);
        }

        if (RestUtil.isError(response.getStatusCode())) {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

        return ResponseEntity.ok().body(response.getBody());
    }

}
