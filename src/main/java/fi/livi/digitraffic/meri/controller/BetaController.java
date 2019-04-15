package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_BETA_BASE_PATH;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.model.sse.SseFeatureCollection;
import fi.livi.digitraffic.meri.service.sse.SseService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(API_BETA_BASE_PATH)
@ConditionalOnWebApplication
public class BetaController {

    private final SseService sseService;

    public BetaController(final SseService sseService) {
        this.sseService = sseService;
    }

    @ApiOperation("Return all SSE (Sea State Estimation) datas as GeoJSON")
    @GetMapping(path = "/sse", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public SseFeatureCollection getAll() {
        return sseService.findAll();
    }

}
