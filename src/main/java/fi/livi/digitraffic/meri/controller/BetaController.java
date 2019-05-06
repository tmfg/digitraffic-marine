package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_BETA_BASE_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_SSE_PATH;
import static fi.livi.digitraffic.meri.model.Constants.ISO_DATE_TIME_FROM_DOC;
import static fi.livi.digitraffic.meri.model.Constants.ISO_DATE_TIME_FROM_VALUE;
import static fi.livi.digitraffic.meri.model.Constants.ISO_DATE_TIME_TO_DOC;
import static fi.livi.digitraffic.meri.model.Constants.ISO_DATE_TIME_TO_VALUE;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.model.sse.SseFeatureCollection;
import fi.livi.digitraffic.meri.service.sse.SseService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(API_BETA_BASE_PATH)
@ConditionalOnWebApplication
public class BetaController {
    private static final Logger log = LoggerFactory.getLogger(BetaController.class);

    private final SseService sseService;

    public static final String LATEST_PATH = "/latest";
    public static final String HISTORY_PATH = "/history";


    public BetaController(final SseService sseService) {
        this.sseService = sseService;
    }

    @ApiOperation("Return latest SSE (Sea State Estimation) data as GeoJSON")
    @GetMapping(path = API_SSE_PATH + LATEST_PATH , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public SseFeatureCollection findLatest() {
        return sseService.findLatest();
    }

    @ApiOperation("Return latest SSE (Sea State Estimation) data as GeoJSON for given site")
    @GetMapping(path = API_SSE_PATH + LATEST_PATH + "/{siteNumber}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public SseFeatureCollection findLatest(
        @ApiParam(value = "SSE site number", required = true)
        @PathVariable("siteNumber")
        final int siteNumber) {

        return sseService.findLatest(siteNumber);
    }

    @ApiOperation("Return SSE history data (Sea State Estimation) data as GeoJSON for given site and time")
    @GetMapping(path = API_SSE_PATH + HISTORY_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public SseFeatureCollection findHistory(

        @ApiParam(value = "Return SSE data after given time in " + ISO_DATE_TIME_FROM_DOC, example = ISO_DATE_TIME_FROM_VALUE)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @RequestParam(value = "from", required = false)
        final ZonedDateTime from,

        @ApiParam(value = "Return SSE data before given time in " + ISO_DATE_TIME_TO_DOC, example = ISO_DATE_TIME_TO_VALUE)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @RequestParam(value = "to", required = false)
        final ZonedDateTime to) {

        return sseService.findHistory(from, to);
    }

    @GetMapping(path = API_SSE_PATH + HISTORY_PATH + "/{siteNumber}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

        return sseService.findHistory(siteNumber, from, to);
    }
}
