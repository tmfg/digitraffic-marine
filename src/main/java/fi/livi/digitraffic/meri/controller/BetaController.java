package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_BETA_BASE_PATH;
import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_WINTER_NAVIGATION_PATH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationDirwayFeatureCollection;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPortFeatureCollection;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationShipFeatureCollection;
import fi.livi.digitraffic.meri.service.winternavigation.WinterNavigationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(API_BETA_BASE_PATH)
public class BetaController {

    private WinterNavigationService winterNavigationService;

    @Autowired
    public BetaController(final WinterNavigationService winterNavigationService) {
        this.winterNavigationService = winterNavigationService;
    }

    @ApiOperation("Return winter navigation ports")
    @GetMapping(path = API_WINTER_NAVIGATION_PATH + "/ports", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful retrieval of winter navigation ports"),
                    @ApiResponse(code = 500, message = "Internal server error") })
    @ResponseBody
    public WinterNavigationPortFeatureCollection getWinterNavigationPorts() {
        return winterNavigationService.getWinterNavigationPorts();
    }

    @ApiOperation("Return winter navigation ships")
    @GetMapping(path = API_WINTER_NAVIGATION_PATH + "/ships", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful retrieval of winter navigation ports"),
                    @ApiResponse(code = 500, message = "Internal server error") })
    @ResponseBody
    public WinterNavigationShipFeatureCollection getWinterNavigationShips() {
        return winterNavigationService.getWinterNavigationShips();
    }

    @ApiOperation("Return winter navigation dirways")
    @GetMapping(path = API_WINTER_NAVIGATION_PATH + "/dirways", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful retrieval of winter navigation dirways"),
                    @ApiResponse(code = 500, message = "Internal server error") })
    @ResponseBody
    public WinterNavigationDirwayFeatureCollection getWinterNavigationDirways() {
        return winterNavigationService.getWinterNavigationDirways();
    }
}
