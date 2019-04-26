package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_BETA_BASE_PATH;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    private final SseService sseService;

    public static final String LATEST_PATH = "/latest";

    public BetaController(final SseService sseService) {
        this.sseService = sseService;
    }

    @ApiOperation("Return latest SSE (Sea State Estimation) data as GeoJSON")
    @GetMapping(path = "/sse" + LATEST_PATH , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public SseFeatureCollection findLatest() {
        return sseService.findLatest();
    }

    @ApiOperation("Return latest SSE (Sea State Estimation) data as GeoJSON for given site")
    @GetMapping(path = LATEST_PATH + "/{siteNumber}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public SseFeatureCollection findLatest(
        @ApiParam(value = "SSE site number", required = true)
        @PathVariable("siteNumber")
        final int siteNumber) {
        return sseService.findLatest(siteNumber);
    }

}
