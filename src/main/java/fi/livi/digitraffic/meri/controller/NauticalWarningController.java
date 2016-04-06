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

import fi.livi.digitraffic.meri.model.GeoJSON;

@RestController
@RequestMapping(API_V1_BASE_PATH)
public class NauticalWarningController {

    private final String POOKI_URL;
    private RestTemplate template = new RestTemplate();

    @Autowired
    public NauticalWarningController(@Value("${ais.pooki.url}") final String pooki_url) {
        POOKI_URL = pooki_url;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/nautical-warnings",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public GeoJSON nauticalWarnings() {
        ResponseEntity<String> response = template.getForEntity(POOKI_URL, String.class);
        return new GeoJSON(response.toString());
    }

}
