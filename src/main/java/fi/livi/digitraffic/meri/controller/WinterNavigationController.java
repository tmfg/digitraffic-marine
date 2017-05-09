package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;
import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_WINTER_NAVIGATION_PATH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPortFeatureCollection;
import fi.livi.digitraffic.meri.service.winternavigation.WinterNavigationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(API_V1_BASE_PATH + API_WINTER_NAVIGATION_PATH)
public class WinterNavigationController {

    private WinterNavigationService winterNavigationService;

    @Autowired
    public WinterNavigationController(final WinterNavigationService winterNavigationService) {
        this.winterNavigationService = winterNavigationService;
    }

    @ApiOperation("Return winter navigation ports.")
    @GetMapping(path = "/ports", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful retrieval of winter navigation ports"),
                    @ApiResponse(code = 500, message = "Internal server error") })
    @ResponseBody
    public WinterNavigationPortFeatureCollection getWinterNavigationPorts() {
        return winterNavigationService.getWinterNavigationPorts();
    }
}
