package fi.livi.digitraffic.meri.controller.winternavigation;

import static fi.livi.digitraffic.meri.controller.ApiConstants.API_WINTER_NAVIGATION;
import static fi.livi.digitraffic.meri.controller.ApiConstants.V1;
import static fi.livi.digitraffic.meri.controller.ApiConstants.WINTER_NAVIGATION_V1_TAG;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.HTTP_INTERNAL_SERVER_ERROR;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.HTTP_OK;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_JSON;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationDirwayFeatureCollectionV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationPortFeatureCollectionV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationPortFeatureV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationShipFeatureCollectionV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationShipFeatureV1;
import fi.livi.digitraffic.meri.service.winternavigation.v1.WinterNavigationWebServiceV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = WINTER_NAVIGATION_V1_TAG, description = "Winter Navigation APIs")
@RestController
@Validated
@ConditionalOnWebApplication
public class WinterNavigationControllerV1 {
    public static final String API_WINTER_NAVIGATION_V1 = API_WINTER_NAVIGATION + V1;
    public static final String PORTS = "/ports";
    public static final String VESSELS = "/vessels";
    public static final String DIRWAYS = "/dirways";
    private final WinterNavigationWebServiceV1 winterNavigationWebServiceV1;

    @Autowired
    public WinterNavigationControllerV1(final WinterNavigationWebServiceV1 winterNavigationWebServiceV1) {
        this.winterNavigationWebServiceV1 = winterNavigationWebServiceV1;
    }

    @Operation(summary = "Return winter navigation ports")
    @GetMapping(path = API_WINTER_NAVIGATION_V1 + PORTS, produces = { MEDIA_TYPE_APPLICATION_JSON,
                                                                      MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                                      MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of winter navigation ports"),
        @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public WinterNavigationPortFeatureCollectionV1 getWinterNavigationPorts() {
        return winterNavigationWebServiceV1.getWinterNavigationPorts();
    }

    @Operation(summary = "Return winter navigation vessels")
    @GetMapping(path = API_WINTER_NAVIGATION_V1 + VESSELS, produces = { MEDIA_TYPE_APPLICATION_JSON,
                                                                        MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                                        MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of winter navigation vessels"),
        @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public WinterNavigationShipFeatureCollectionV1 getWinterNavigationShips() {
        return winterNavigationWebServiceV1.getWinterNavigationShips();
    }

    @Operation(summary = "Return winter navigation dirways")
    @GetMapping(path = API_WINTER_NAVIGATION_V1 + DIRWAYS, produces = { MEDIA_TYPE_APPLICATION_JSON,
                                                                          MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                                          MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of winter navigation dirways"),
        @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public WinterNavigationDirwayFeatureCollectionV1 getWinterNavigationDirways() {
        return winterNavigationWebServiceV1.getWinterNavigationDirways();
    }

    @Operation(summary = "Return winter navigation vessel")
    @GetMapping(path = API_WINTER_NAVIGATION_V1 + VESSELS +"/{vesselId}", produces = { MEDIA_TYPE_APPLICATION_JSON,
        MEDIA_TYPE_APPLICATION_GEO_JSON,
        MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of a winter navigation vesseö"),
        @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public WinterNavigationShipFeatureV1 getWinterNavigationShipByVesselId(@Parameter(description = "Vessel identification code. Equals " +
    "IMO-{IMO-code} when vessel IMO is present. Otherwise MMSI-{MMSI-code} (Maritime Mobile Service Identity).", required = true)
    @PathVariable("vesselId") final String vesselId) {

        return winterNavigationWebServiceV1.getWinterNavigationShipByVesselId(vesselId);
    }

    @Operation(summary = "Return winter navigation port")
    @GetMapping(path = API_WINTER_NAVIGATION_V1 + PORTS + "/{locode}", produces = { MEDIA_TYPE_APPLICATION_JSON,
        MEDIA_TYPE_APPLICATION_GEO_JSON,
        MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of a winter navigation port"),
        @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public WinterNavigationPortFeatureV1 getWinterNavigationPortByLocode(@Parameter(description = "Port locode", required = true)
    @PathVariable("locode") final String locode) {
        return winterNavigationWebServiceV1.getWinterNavigationPortByLocode(locode);
    }
}
