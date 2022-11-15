package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static fi.livi.digitraffic.meri.controller.ApiDeprecations.API_NOTE_2023_06_01;
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

import fi.livi.digitraffic.meri.annotation.Sunset;
import fi.livi.digitraffic.meri.controller.exception.PookiException;
import fi.livi.digitraffic.meri.model.pooki.PookiFeatureCollection;
import fi.livi.digitraffic.meri.service.BadRequestException;
import fi.livi.digitraffic.meri.service.nauticalwarning.NauticalWarningService;
import fi.livi.digitraffic.meri.service.nauticalwarning.NauticalWarningService.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Deprecated(forRemoval = true)
@Sunset(date = ApiDeprecations.SUNSET_2023_06_01)
@RequestMapping(API_V1_BASE_PATH)
@ConditionalOnWebApplication
@Tag(name = "nautical-warning-controller", description = "Nautical Warning Controller. " + API_NOTE_2023_06_01)
public class NauticalWarningController {

    private static final Logger log = LoggerFactory.getLogger(NauticalWarningController.class);
    private final NauticalWarningService nauticalWarningService;

    @Autowired
    public NauticalWarningController(final NauticalWarningService nauticalWarningService) {
        this.nauticalWarningService = nauticalWarningService;
    }

    @Deprecated(forRemoval = true)
    @Sunset(date = ApiDeprecations.SUNSET_2023_04_01)
    @Operation(summary = "Return nautical warnings of given status. " + API_NOTE_2023_06_01)
    @RequestMapping(method = RequestMethod.GET, path = "/nautical-warnings/{status}",
                    produces = { MEDIA_TYPE_APPLICATION_JSON,
                                 MEDIA_TYPE_APPLICATION_GEO_JSON,
                                 MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public PookiFeatureCollection nauticalWarnings(@Parameter(description = "Status", required = true, schema = @Schema(allowableValues = {"published", "archived"}))
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
