package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_WINTER_NAVIGATION_PATH;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_JSON;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(API_V1_BASE_PATH + API_WINTER_NAVIGATION_PATH)
@ConditionalOnWebApplication
@Tag(name= "winter-navigation-controller", description = "Winter Navigation Controller")
public class WinterNavigationController {

    private WinterNavigationService winterNavigationService;

    @Autowired
    public WinterNavigationController(final WinterNavigationService winterNavigationService) {
        this.winterNavigationService = winterNavigationService;
    }

    @Operation(summary = "Return winter navigation ports")
    @GetMapping(path = "/ports", produces = { MEDIA_TYPE_APPLICATION_JSON,
                                              MEDIA_TYPE_APPLICATION_GEO_JSON,
                                              MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successful retrieval of winter navigation ports"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
    @ResponseBody
    public WinterNavigationPortFeatureCollection getWinterNavigationPorts() {
        return winterNavigationService.getWinterNavigationPorts();
    }

    @Operation(summary = "Return winter navigation ships")
    @GetMapping(path = "/ships", produces = { MEDIA_TYPE_APPLICATION_JSON,
                                              MEDIA_TYPE_APPLICATION_GEO_JSON,
                                              MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successful retrieval of winter navigation ships"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
    @ResponseBody
    public WinterNavigationShipFeatureCollection getWinterNavigationShips() {
        return winterNavigationService.getWinterNavigationShips();
    }

    @Operation(summary = "Return winter navigation dirways")
    @GetMapping(path = "/dirways", produces = { MEDIA_TYPE_APPLICATION_JSON,
                                                MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successful retrieval of winter navigation dirways"),
                    @ApiResponse(responseCode = "500", description = "Internal server error") })
    @ResponseBody
    public WinterNavigationDirwayFeatureCollection getWinterNavigationDirways() {
        return winterNavigationService.getWinterNavigationDirways();
    }

    @Operation(summary = "Return winter navigation ship")
    @GetMapping(path = "/ships/{vesselId}", produces = { MEDIA_TYPE_APPLICATION_JSON,
                                                         MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                         MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successful retrieval of a winter navigation ship"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
    @ResponseBody
    public WinterNavigationShipFeature getWinterNavigationShipByVesselId(@Parameter(description = "Vessel identification code. Equals " +
        "IMO-{IMO-code} when vessel IMO is present. Otherwise MMSI-{MMSI-code} (Maritime Mobile Service Identity).", required = true)
                                                                         @PathVariable("vesselId") final String vesselId) {

        return winterNavigationService.getWinterNavigationShipByVesselId(vesselId);
    }

    @Operation(summary = "Return winter navigation port")
    @GetMapping(path = "/ports/{locode}", produces = { MEDIA_TYPE_APPLICATION_JSON,
                                                       MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                       MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successful retrieval of a winter navigation port"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
    @ResponseBody
    public WinterNavigationPortFeature getWinterNavigationPortByLocode(@Parameter(description = "Port locode", required = true)
                                                                       @PathVariable("locode") final String locode) {

        return winterNavigationService.getWinterNavigationPortByLocode(locode);
    }
}

