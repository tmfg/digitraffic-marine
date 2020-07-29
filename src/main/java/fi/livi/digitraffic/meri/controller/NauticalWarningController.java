package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
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
@ConditionalOnWebApplication
public class NauticalWarningController {
    private final RestTemplate restTemplate;

    private String pookiUrl;

    private static final Logger log = LoggerFactory.getLogger(NauticalWarningController.class);

    public enum Status {
        PUBLISHED("merivaroitus_julkaistu_dt"),
        ARCHIVED("merivaroitus_arkistoitu_dt");

        public final String layer;

        Status(final String layer){
            this.layer = layer;
        }

        public static boolean contains(final String needle) {
            for (final Status status : values()) {
                if (StringUtils.equalsIgnoreCase(status.name(), needle)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Autowired
    public NauticalWarningController(@Value("${dt.pooki.url}") final String pookiUrl) {
        this.pookiUrl = pookiUrl;
        final HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());
        this.restTemplate = new RestTemplate(clientHttpRequestFactory);
    }

    @ApiOperation("Return nautical warnings of given status.")
    @RequestMapping(method = RequestMethod.GET, path = "/nautical-warnings/{status}",
                    produces = { MEDIA_TYPE_APPLICATION_JSON_UTF8,
                                 MEDIA_TYPE_APPLICATION_GEO_JSON,
                                 MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public PookiFeatureCollection nauticalWarnings(@ApiParam(value = "Status", required = true, allowableValues = "published,archived" )
                                              @PathVariable final String status) throws PookiException {

        // Parse status argument
        if (!Status.contains(status)) {
            throw new BadRequestException("Illegal argument for status");
        }
        final Status s = Status.valueOf(status.toUpperCase());

        // Call Pooki and parse response to GeoJSON
        final String url = getUrlFor(s);
        try {
            return getObjectFromUrl(url);
        } catch (final Exception e1) {
            // TODO DPO-1126 tässä parametrina e1 poikkeus, joka lokittaa kantaan = avain arvo pareja. Pitäisikö
            // ottaa stack trace ja suodattaa se?
            log.info("Exception-1 from pooki server for pookiUrl=" + url, e1);
            // Retry once, because Pooki sometimes responds first time with error.
            try {
                return getObjectFromUrl(url);
            } catch(final Exception e2) {
                log.info("Exception-2 from pooki server for pookiUrl=" + url, e2);
                throw new PookiException("Bad Gateway. Pooki responded twice with error response.");
            }
        }
    }

    private PookiFeatureCollection getObjectFromUrl(final String url) {
        final PookiFeatureCollection collection = restTemplate.getForObject(url, PookiFeatureCollection.class);

        return collection;
    }

    private String getUrlFor(final Status status) {
        return String.format("%s?crs=EPSG:4326&layer=%s", pookiUrl, status.layer);
    }

    public void setPookiUrl(final String pookiUrl) {
        this.pookiUrl = pookiUrl;
    }
}
