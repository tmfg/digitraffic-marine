package fi.livi.digitraffic.meri.controller.portnet;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_METADATA_PART_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.HTTP_INTERNAL_SERVER_ERROR;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.HTTP_NOT_FOUND;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.HTTP_OK;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

import java.time.ZonedDateTime;
import java.util.List;

import fi.livi.digitraffic.meri.controller.ApiDeprecations;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.controller.MediaTypes;
import fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails;
import fi.livi.digitraffic.meri.model.portnet.metadata.CodeDescriptions_V1;
import fi.livi.digitraffic.meri.model.portnet.metadata.LocationFeatureCollections_V1;
import fi.livi.digitraffic.meri.service.portnet.PortnetMetadataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(API_V1_BASE_PATH + API_METADATA_PART_PATH)
@ConditionalOnWebApplication
@Tag(name = "portnet-metadata-controller", description = "Portnet Metadata Controller. " + ApiDeprecations.API_NOTE_FUTURE)
public class PortnetMetadataController {
    public static final String CODE_DESCRIPTIONS = "/code-descriptions";
    public static final String SSN_LOCATIONS_PATH =  "/locations";
    public static final String SSN_LOCATIONS_BY_COUNTRY_PATH =  "/locations-by-country";
    public static final String VESSEL_DETAILS_PATH = "/vessel-details";

    private final PortnetMetadataService portnetMetadataService;

    public PortnetMetadataController(final PortnetMetadataService portnetMetadataService) {
        this.portnetMetadataService = portnetMetadataService;
    }

    @Deprecated(forRemoval = true, since = ApiDeprecations.SINCE_FUTURE)
    @Operation(summary = "Return all code descriptions. " + ApiDeprecations.API_NOTE_FUTURE)
    @GetMapping(path = CODE_DESCRIPTIONS, produces = MediaTypes.MEDIA_TYPE_APPLICATION_JSON)
    @ResponseBody
    public CodeDescriptions_V1 listCodeDescriptions() {
        return portnetMetadataService.listCodeDescriptions();
    }

    @Deprecated(forRemoval = true, since = ApiDeprecations.SINCE_FUTURE)
    @Operation(summary = "Return list of all berths, port areas and locations. " + ApiDeprecations.API_NOTE_FUTURE)
    @GetMapping(path = SSN_LOCATIONS_PATH, produces = MediaTypes.MEDIA_TYPE_APPLICATION_JSON)
    @ResponseBody
    public LocationFeatureCollections_V1 listAllMetadata() {
        return portnetMetadataService.listaAllMetadata();
    }

    @Deprecated(forRemoval = true, since = ApiDeprecations.SINCE_FUTURE)
    @Operation(summary = "Return one location's berths, port areas and location by SafeSeaNet location code. " + ApiDeprecations.API_NOTE_FUTURE)
    @GetMapping(path = SSN_LOCATIONS_PATH + "/{locode}", produces = MediaTypes.MEDIA_TYPE_APPLICATION_JSON)
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of ssn location"),
                    @ApiResponse(responseCode = HTTP_NOT_FOUND, description = "Ssn location not found", content = @Content),
                    @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public LocationFeatureCollections_V1 findSsnLocationByLocode(@PathVariable(value = "locode", required = true) final String locode) {
        return portnetMetadataService.findSsnLocationByLocode(locode);
    }

    @Deprecated(forRemoval = true, since = ApiDeprecations.SINCE_FUTURE)
    @Operation(summary = "Return list of SafeSeaNet locations by country name. " + ApiDeprecations.API_NOTE_FUTURE)
    @GetMapping(path = SSN_LOCATIONS_BY_COUNTRY_PATH + "/{country}", produces = MediaTypes.MEDIA_TYPE_APPLICATION_JSON)
    @ResponseBody
    public LocationFeatureCollections_V1 findSsnLocationsByCountry(@PathVariable(value = "country", required = true) final String country) {
        return portnetMetadataService.findSsnLocationsByCountry(country);
    }

    @Deprecated(forRemoval = true, since = ApiDeprecations.SINCE_FUTURE)
    @Operation(summary = "Return list of vessels details. " + ApiDeprecations.API_NOTE_FUTURE)
    @GetMapping(path = VESSEL_DETAILS_PATH, produces = MediaTypes.MEDIA_TYPE_APPLICATION_JSON)
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of vessel details"),
                    @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public List<VesselDetails> findVesselDetails(
            @Parameter(description = "Return details of vessels whose metadata has changed after given time in ISO date format {yyyy-MM-dd'T'HH:mm:ss.SSSZ} e.g. 2016-10-31T06:30:00.000Z. " +
                      "Default value is now minus 24 hours if all parameters are empty.")
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DATE_TIME) ZonedDateTime from,

            @Parameter(description = "Return vessel details for given vessel name")
            @RequestParam(value = "vesselName", required = false) final String vesselName,

            @Parameter(description = "Return vessel details for given mmsi")
            @RequestParam(value = "mmsi", required = false) final Integer mmsi,

            @Parameter(description = "Return vessel details for given IMO/LLOYDS")
            @RequestParam(value = "imo", required = false) final Integer imo,

            @Parameter(description = "Return vessel details for vessels with given nationality or nationalities (e.g. FI)")
            @RequestParam(value = "nationality", required = false) final List<String> nationalities,

            @Parameter(description = "Return vessel details for given vessel type code")
            @RequestParam(value = "vesselTypeCode", required = false) final Integer vesselTypeCode) {

        if (!ObjectUtils.anyNotNull(from, vesselName, mmsi, imo, nationalities, vesselTypeCode)) {
            from = ZonedDateTime.now().minusDays(1L);
        }

        return portnetMetadataService.findVesselDetails(from, vesselName, mmsi, imo, nationalities, vesselTypeCode);
    }
}
