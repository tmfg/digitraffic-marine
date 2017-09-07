package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;

import org.apache.commons.lang3.StringUtils;
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

import fi.livi.digitraffic.meri.controller.exception.PookiException;
import fi.livi.digitraffic.meri.model.pooki.PookiFeatureCollection;
import fi.livi.digitraffic.meri.service.BadRequestException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(API_V1_BASE_PATH)
public class NauticalWarningController {
    private final RestTemplate restTemplate;

    private String pookiUrl;

    public enum Status {
        PUBLISHED("merivaroitus_julkaistu_dt"),
        ARCHIVED("merivaroitus_arkistoitu_dt");

        public final String layer;

        Status(final String layer){
            this.layer = layer;
        }

        public static boolean contains(String needle) {
            for (Status status : values()) {
                if (StringUtils.equalsIgnoreCase(status.name(), needle)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Autowired
    public NauticalWarningController(@Value("${ais.pooki.url}") final String pookiUrl) {
        this.pookiUrl = pookiUrl;
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());
        this.restTemplate = new RestTemplate(clientHttpRequestFactory);
    }

    @ApiOperation("Return nautical warnings of given status.")
    @RequestMapping(method = RequestMethod.GET, path = "/nautical-warnings/{status}",
                    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public PookiFeatureCollection nauticalWarnings(@ApiParam(value = "Status", required = true, allowableValues = "published,archived" )
                                              @PathVariable final String status) throws BadRequestException, PookiException {

        // Parse status argument
        if (!Status.contains(status)) {
            throw new BadRequestException("Illegal argument for status");
        }
        final Status s = Status.valueOf(status.toUpperCase());

        // Call Pooki and parse response to GeoJSON
        try {
            return getObjectFromUrl(s);
        } catch (Exception e) {
            // Retry once, because Pooki sometimes responds first time with error.
            try {
                return getObjectFromUrl(s);
            } catch(Exception e1) {
                throw new PookiException("Bad Gateway. Pooki responded twice with error response.");
            }
        }
    }

    private PookiFeatureCollection getObjectFromUrl(final Status status) {
        final String url = String.format("%s?crs=EPSG:4326&layer=%s", pookiUrl, status.layer);
        return restTemplate.getForObject(url, PookiFeatureCollection.class);
    }

    public void setPookiUrl(final String pookiUrl) {
        this.pookiUrl = pookiUrl;
    }
}
