package fi.livi.digitraffic.meri.controller.ais;

import static fi.livi.digitraffic.meri.controller.ApiConstants.AIS_V1_TAG;
import static fi.livi.digitraffic.meri.controller.ApiConstants.API_AIS;
import static fi.livi.digitraffic.meri.controller.ApiConstants.V1;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.HTTP_INTERNAL_SERVER_ERROR;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.HTTP_NOT_FOUND;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.HTTP_OK;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_JSON;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.controller.MediaTypes;
import fi.livi.digitraffic.meri.controller.ResponseEntityWithLastModifiedHeader;
import fi.livi.digitraffic.meri.dto.LastModifiedSupport;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeatureCollection;
import fi.livi.digitraffic.meri.model.ais.VesselMetadataJson;
import fi.livi.digitraffic.meri.service.ais.VesselLocationService;
import fi.livi.digitraffic.meri.service.ais.VesselMetadataService;
import fi.livi.digitraffic.meri.util.NullUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = AIS_V1_TAG, description = "Automatic Identification System (AIS) APIs")
@RestController
@Validated
@ConditionalOnWebApplication
public class AisControllerV1 {
    public static final String API_AIS_V1 = API_AIS + V1;
    public static final String LOCATIONS = "/locations";
    public static final String VESSELS = "/vessels";

    private final VesselLocationService vesselLocationService;
    private final VesselMetadataService vesselMetadataService;

    public AisControllerV1(final VesselLocationService vesselLocationService,
                           final VesselMetadataService vesselMetadataService) {
        this.vesselLocationService = vesselLocationService;
        this.vesselMetadataService = vesselMetadataService;
    }

    @Operation(summary = "Find latest vessel locations by mmsi and optional timestamp interval in milliseconds from Unix epoch.")
    @GetMapping(path = API_AIS_V1 + LOCATIONS, produces = { MEDIA_TYPE_APPLICATION_JSON,
                                                            MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                            MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public VesselLocationFeatureCollection vesselLocationsByMssiAndTimestamp(
        @Parameter(description = "Maritime Mobile Service Identity (MMSI)")
        @RequestParam(value = "mmsi", required = false)
        final Integer mmsi,
        @Parameter(description = "From timestamp timestamp in milliseconds from Unix epoch 1970-01-01T00:00:00Z")
        @RequestParam(value = "from", required = false)
        final Long from,
        @Parameter(description = "To timestamp")
        @RequestParam(value = "to", required = false)
        final Long to,
        @Parameter(description = "Radius of search circle in kilometers (km) using haversine formula.")
        @RequestParam(value = "radius", required = false)
        final Double radius,
        @Parameter(description = "Latitude of the point")
        @RequestParam(value = "latitude", required = false)
        final Double latitude,
        @Parameter(description = "Longitude of the point")
        @RequestParam(value = "longitude", required = false)
        final Double longitude) {

        if(!NullUtil.allNullOrNoneNull(radius, latitude, longitude)) {
            throw new IllegalArgumentException("To find vessels within a circle all parameters radius, latitude and longitude must be given");
        }

        if(radius != null) {
            if (mmsi != null) {
                throw new IllegalArgumentException("Circle search does not support mmsi");
            }

            return vesselLocationService.findAllowedLocationsWithinRadiusFromPoint(radius, latitude, longitude, from, to);
        }

        return vesselLocationService.findAllowedLocations(mmsi, from, to);
    }

    @Operation(summary = "Return latest vessel metadata by mmsi.")
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of vessel metadata"),
        @ApiResponse(responseCode = HTTP_NOT_FOUND, description = "Vessel metadata not found", content = @Content),
        @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @GetMapping(path = API_AIS_V1 + VESSELS +"/{mmsi}", produces = MediaTypes.MEDIA_TYPE_APPLICATION_JSON)
    @ResponseBody
    public VesselMetadataJson vesselMetadataByMssi(@PathVariable("mmsi") final int mmsi) {
        return vesselMetadataService.findAllowedMetadataByMssi(mmsi);
    }

    @Operation(summary = "Return latest vessel metadata for all known vessels.")
    @GetMapping(path = API_AIS_V1 + VESSELS, produces = MediaTypes.MEDIA_TYPE_APPLICATION_JSON)
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of vessel metadata"),
        @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public ResponseEntityWithLastModifiedHeader<List<VesselMetadataJson>> allVessels(@Parameter(description = "From timestamp timestamp in milliseconds from Unix epoch 1970-01-01T00:00:00Z")
                                               @RequestParam(value = "from", required = false)
                                               final Long from,
                                               @Parameter(description = "To timestamp")
                                               @RequestParam(value = "to", required = false)
                                               final Long to) {
        final List<VesselMetadataJson> vms = vesselMetadataService.findAllowedVesselMetadataFrom(from, to);
        final Instant lastModified = vms.stream().map(LastModifiedSupport::getLastModified).max(Comparator.comparing(Function.identity())).orElse(Instant.EPOCH);
        return ResponseEntityWithLastModifiedHeader.of(vms, lastModified);
    }
}
