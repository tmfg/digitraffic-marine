package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_WINTER_NAVIGATION_PATH;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationDirwayFeatureCollection;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPortFeature;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPortFeatureCollection;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationShipFeature;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationShipFeatureCollection;
import fi.livi.digitraffic.meri.service.winternavigation.WinterNavigationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation("Return winter navigation ports")
    @GetMapping(path = "/ports", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful retrieval of winter navigation ports"),
                    @ApiResponse(code = 500, message = "Internal server error") })
    @ResponseBody
    public WinterNavigationPortFeatureCollection getWinterNavigationPorts() {
        return winterNavigationService.getWinterNavigationPorts();
    }

    @ApiOperation("Return winter navigation ships")
    @GetMapping(path = "/ships", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful retrieval of winter navigation ships"),
                    @ApiResponse(code = 500, message = "Internal server error") })
    @ResponseBody
    public WinterNavigationShipFeatureCollection getWinterNavigationShips() {
        return winterNavigationService.getWinterNavigationShips();
    }

    @ApiOperation("Return winter navigation dirways")
    @GetMapping(path = "/dirways", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful retrieval of winter navigation dirways"),
                    @ApiResponse(code = 500, message = "Internal server error") })
    @ResponseBody
    public WinterNavigationDirwayFeatureCollection getWinterNavigationDirways() {
        return winterNavigationService.getWinterNavigationDirways();
    }

    @ApiOperation("Return winter navigation ship")
    @GetMapping(path = "/ships/{vesselId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful retrieval of a winter navigation ship"),
                    @ApiResponse(code = 500, message = "Internal server error") })
    @ResponseBody
    public WinterNavigationShipFeature getWinterNavigationShipByVesselId(@ApiParam(value = "Vessel identification code. Equals " +
        "IMO-{IMO-code} when vessel IMO is present. Otherwise MMSI-{MMSI-code} (Maritime Mobile Service Identity).", required = true)
                                                                         @PathVariable("vesselId") final String vesselId) {

        return winterNavigationService.getWinterNavigationShipByVesselId(vesselId);
    }

    @ApiOperation("Return winter navigation port")
    @GetMapping(path = "/ports/{locode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful retrieval of a winter navigation port"),
                    @ApiResponse(code = 500, message = "Internal server error") })
    @ResponseBody
    public WinterNavigationPortFeature getWinterNavigationPortByLocode(@ApiParam(value = "Port locode", required = true)
                                                                       @PathVariable("locode") final String locode) {

        return winterNavigationService.getWinterNavigationPortByLocode(locode);
    }
}
