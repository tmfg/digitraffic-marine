package fi.livi.digitraffic.meri.controller.winternavigation;

import fi.livi.digitraffic.meri.model.winternavigation.*;
import fi.livi.digitraffic.meri.service.winternavigation.WinterNavigationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static fi.livi.digitraffic.meri.controller.ApiConstants.*;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.HTTP_INTERNAL_SERVER_ERROR;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.HTTP_OK;
import static fi.livi.digitraffic.meri.controller.MediaTypes.*;

@Tag(name = WINTER_NAVIGATION_V1_TAG, description = "Winter Navigation Controller")
@RestController
@Validated
@ConditionalOnWebApplication
public class WinterNavigationControllerV1 {
    private static final String API_WINTER_NAVIGATION_V1 = API_WINTER_NAVIGATION + V1;
    private final WinterNavigationService winterNavigationService;

    @Autowired
    public WinterNavigationControllerV1(final WinterNavigationService winterNavigationService) {
        this.winterNavigationService = winterNavigationService;
    }

    @Operation(summary = "Return winter navigation ports")
    @GetMapping(path = API_WINTER_NAVIGATION_V1 + "/ports", produces = { MEDIA_TYPE_APPLICATION_JSON,
        MEDIA_TYPE_APPLICATION_GEO_JSON,
        MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of winter navigation ports"),
        @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public WinterNavigationPortFeatureCollection getWinterNavigationPorts() {
        return winterNavigationService.getWinterNavigationPorts();
    }

    @Operation(summary = "Return winter navigation vessels")
    @GetMapping(path = "/vessels", produces = { MEDIA_TYPE_APPLICATION_JSON,
        MEDIA_TYPE_APPLICATION_GEO_JSON,
        MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of winter navigation vessels"),
        @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public WinterNavigationShipFeatureCollection getWinterNavigationShips() {
        return winterNavigationService.getWinterNavigationShips();
    }

    @Operation(summary = "Return winter navigation dirways")
    @GetMapping(path = "/dirways", produces = { MEDIA_TYPE_APPLICATION_JSON,
        MEDIA_TYPE_APPLICATION_GEO_JSON,
        MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of winter navigation dirways"),
        @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public WinterNavigationDirwayFeatureCollection getWinterNavigationDirways() {
        return winterNavigationService.getWinterNavigationDirways();
    }

    @Operation(summary = "Return winter navigation vessel")
    @GetMapping(path = "/vessels/{vesselId}", produces = { MEDIA_TYPE_APPLICATION_JSON,
        MEDIA_TYPE_APPLICATION_GEO_JSON,
        MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of a winter navigation vesseö"),
        @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
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
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of a winter navigation port"),
        @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public WinterNavigationPortFeature getWinterNavigationPortByLocode(@Parameter(description = "Port locode", required = true)
    @PathVariable("locode") final String locode) {
        return winterNavigationService.getWinterNavigationPortByLocode(locode);
    }
}
