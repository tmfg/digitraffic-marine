package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_JSON;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.controller.exception.PookiException;
import fi.livi.digitraffic.meri.model.pooki.PookiFeatureCollection;
import fi.livi.digitraffic.meri.service.BadRequestException;
import fi.livi.digitraffic.meri.service.nauticalwarning.NauticalWarningService;
import fi.livi.digitraffic.meri.service.nauticalwarning.NauticalWarningService.Status;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(API_V1_BASE_PATH)
@ConditionalOnWebApplication
public class NauticalWarningController {

    private static final Logger log = LoggerFactory.getLogger(NauticalWarningController.class);
    private final NauticalWarningService nauticalWarningService;

    @Autowired
    public NauticalWarningController(final NauticalWarningService nauticalWarningService) {
        this.nauticalWarningService = nauticalWarningService;
    }

    @ApiOperation("Return nautical warnings of given status.")
    @RequestMapping(method = RequestMethod.GET, path = "/nautical-warnings/{status}",
                    produces = { MEDIA_TYPE_APPLICATION_JSON,
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

        try {
            return nauticalWarningService.getResponseFor(s);
        } catch(final Exception e) {
            log.error(String.format("Error while calling pooki server for pookiUrl=%s",
                                    nauticalWarningService.getUrlFor(s)));
            throw new PookiException("Bad Gateway. Pooki responded with error response.", e);
        }
    }
}
