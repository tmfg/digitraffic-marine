package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import fi.livi.digitraffic.meri.controller.exception.NauticalWarningErrorHandler;
import fi.livi.digitraffic.meri.model.pooki.FeatureCollection;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(API_V1_BASE_PATH)
public class NauticalWarningController {
    private final RestTemplate restTemplate;

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
        // Enable http gzip compression
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());
        this.restTemplate = new RestTemplate(clientHttpRequestFactory);
        restTemplate.setErrorHandler(new NauticalWarningErrorHandler());
    }

    @ApiOperation("BETA: Return nautical warnings of given status.")
    @RequestMapping(method = RequestMethod.GET, path = "/nautical-warnings/{status}",
                    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public FeatureCollection nauticalWarnings(@ApiParam(value = "Status", required = true, allowableValues = "DRAFT,PUBLISHED,ARCHIVED" )
                                              @PathVariable final String status) {
        try {
            return getObjectFromUrl(status.toUpperCase());
        } catch (Exception e) {
            // Retry once
            return getObjectFromUrl(status.toUpperCase());
        }
    }

    private FeatureCollection getObjectFromUrl(final String status) {
        final Status s = Status.valueOf(status.toUpperCase());
        final String url = String.format("%s?crs=EPSG:4326&layer=%s", pookiUrl, s.layer);
        return restTemplate.getForObject(url, FeatureCollection.class);
    }

    public void setPookiUrl(final String pookiUrl) {
        this.pookiUrl = pookiUrl;
    }
}
