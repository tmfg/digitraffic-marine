package fi.livi.digitraffic.meri.controller.sse;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_SSE_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static fi.livi.digitraffic.meri.model.Constants.ISO_DATE_TIME_FROM_DOC;
import static fi.livi.digitraffic.meri.model.Constants.ISO_DATE_TIME_FROM_VALUE;
import static fi.livi.digitraffic.meri.model.Constants.ISO_DATE_TIME_TO_DOC;
import static fi.livi.digitraffic.meri.model.Constants.ISO_DATE_TIME_TO_VALUE;
import static fi.livi.digitraffic.meri.util.TimeUtil.toInstant;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.controller.MediaTypes;
import fi.livi.digitraffic.meri.model.sse.SseFeatureCollection;
import fi.livi.digitraffic.meri.service.sse.SseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api
@RestController
@RequestMapping(API_V1_BASE_PATH + API_SSE_PATH)
@ConditionalOnWebApplication
public class SseController {

    private static final Logger log = LoggerFactory.getLogger(SseController.class);

    public static final String CODE_400_SEARCH_RESULT_TOO_BIG = "The search result is too big (over 1000 items)";
    public static final String CODE_400_NOT_EXISTS_WITH_IDENTIFIER = "Objects not exists with given identifier";
    public static final String CODE_400_ILLEGAL_ARGUMENTS = "Illegal arguments";

    public static final String LATEST_PATH = "/latest";
    public static final String HISTORY_PATH = "/history";

    private final SseService sseService;

    public SseController(final SseService sseService) {
        this.sseService = sseService;
    }

    @ApiOperation("Return latest SSE (Sea State Estimation) data as GeoJSON")
    @GetMapping(path = LATEST_PATH , produces = { MediaTypes.MEDIA_TYPE_APPLICATION_JSON,
                                                  MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                  MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public SseFeatureCollection findLatest() {
        return sseService.findLatest();
    }

    @ApiOperation("Return latest SSE (Sea State Estimation) data as GeoJSON for given site")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = CODE_400_NOT_EXISTS_WITH_IDENTIFIER)
    })
    @GetMapping(path = LATEST_PATH + "/{siteNumber}", produces = { MediaTypes.MEDIA_TYPE_APPLICATION_JSON,
                                                                   MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                                   MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public SseFeatureCollection findLatest(
        @ApiParam(value = "SSE site number", required = true)
        @PathVariable("siteNumber")
        final int siteNumber) {

        return sseService.findLatest(siteNumber);
    }

    @ApiOperation("Return SSE history data (Sea State Estimation) data as GeoJSON for given time")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = CODE_400_SEARCH_RESULT_TOO_BIG + " or " +
            CODE_400_ILLEGAL_ARGUMENTS)
    })
    @GetMapping(path = HISTORY_PATH, produces = { MediaTypes.MEDIA_TYPE_APPLICATION_JSON,
                                                  MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                  MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public SseFeatureCollection findHistory(

        @ApiParam(value = "Return SSE data after given time in " + ISO_DATE_TIME_FROM_DOC, example = ISO_DATE_TIME_FROM_VALUE, required = true)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @RequestParam(value = "from")
        final ZonedDateTime from,

        @ApiParam(value = "Return SSE data before given time in " + ISO_DATE_TIME_TO_DOC, example = ISO_DATE_TIME_TO_VALUE, required = true)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @RequestParam(value = "to")
        final ZonedDateTime to) {

        return sseService.findHistory(toInstant(from), toInstant(to));
    }

    @ApiOperation("Return SSE history data (Sea State Estimation) data as GeoJSON for given site and time")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = CODE_400_SEARCH_RESULT_TOO_BIG + " or " +
            CODE_400_NOT_EXISTS_WITH_IDENTIFIER + " or " +
            CODE_400_ILLEGAL_ARGUMENTS)
    })
    @GetMapping(path = HISTORY_PATH + "/{siteNumber}", produces = { MediaTypes.MEDIA_TYPE_APPLICATION_JSON,
                                                                    MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                                    MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public SseFeatureCollection findHistoryBySite(
        @ApiParam(value = "SSE site number", required = true)
        @PathVariable(value = "siteNumber")
        final Integer siteNumber,

        @ApiParam(value = "Return SSE data after given time in " + ISO_DATE_TIME_FROM_DOC, example = ISO_DATE_TIME_FROM_VALUE)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @RequestParam(value = "from", required = false)
        final ZonedDateTime from,

        @ApiParam(value = "Return SSE data before given time in " + ISO_DATE_TIME_TO_DOC, example = ISO_DATE_TIME_TO_VALUE)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @RequestParam(value = "to", required = false)
        final ZonedDateTime to) {

        return sseService.findHistory(siteNumber, toInstant(from), toInstant(to));
    }
}
