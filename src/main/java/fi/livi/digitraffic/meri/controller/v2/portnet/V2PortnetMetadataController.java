package fi.livi.digitraffic.meri.controller.v2.portnet;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_METADATA_PART_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V2_BASE_PATH;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.HTTP_INTERNAL_SERVER_ERROR;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.HTTP_NOT_FOUND;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.HTTP_OK;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

import java.time.ZonedDateTime;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails;
import fi.livi.digitraffic.meri.model.portnet.metadata.LocationFeatureCollections;
import fi.livi.digitraffic.meri.model.v2.portnet.metadata.V2CodeDescriptions;
import fi.livi.digitraffic.meri.service.v2.portnet.V2PortnetMetadataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(API_V2_BASE_PATH + API_METADATA_PART_PATH)
@ConditionalOnWebApplication
@Tag(name = "v-2-portnet-metadata-controller", description = "V 2 Portnet Metadata Controller")
public class V2PortnetMetadataController {
    public static final String CODE_DESCRIPTIONS = "/code-descriptions";
    public static final String SSN_LOCATIONS_PATH =  "/locations";
    public static final String SSN_LOCATIONS_BY_COUNTRY_PATH =  "/locations-by-country";
    public static final String VESSEL_DETAILS_PATH = "/vessel-details";

    private final V2PortnetMetadataService v2PortnetMetadataService;

    public V2PortnetMetadataController(final V2PortnetMetadataService v2PortnetMetadataService) {
        this.v2PortnetMetadataService = v2PortnetMetadataService;
    }

    @Operation(summary = "Return all code descriptions.")
    @GetMapping(path = CODE_DESCRIPTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public V2CodeDescriptions listCodeDescriptions() {
        return v2PortnetMetadataService.listCodeDescriptions();
    }

    @Operation(summary = "Return list of all berths, port areas and locations.")
    @GetMapping(path = SSN_LOCATIONS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public LocationFeatureCollections listAllMetadata() {
        return v2PortnetMetadataService.listaAllMetadata();
    }

    @Operation(summary = "Return one location's berths, port areas and location by SafeSeaNet location code.")
    @GetMapping(path = SSN_LOCATIONS_PATH + "/{locode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of ssn location"),
                    @ApiResponse(responseCode = HTTP_NOT_FOUND, description = "Ssn location not found", content = @Content),
                    @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public LocationFeatureCollections findSsnLocationByLocode(@PathVariable(value = "locode", required = true) final String locode) {
        return v2PortnetMetadataService.findSsnLocationByLocode(locode);
    }

    @Operation(summary = "Return list of SafeSeaNet locations by country name")
    @GetMapping(path = SSN_LOCATIONS_BY_COUNTRY_PATH + "/{country}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public LocationFeatureCollections findSsnLocationsByCountry(@PathVariable(value = "country", required = true) final String country) {
        return v2PortnetMetadataService.findSsnLocationsByCountry(country);
    }

    @Operation(summary = "Return list of vessels details")
    @GetMapping(path = VESSEL_DETAILS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
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

        return v2PortnetMetadataService.findVesselDetails(from, vesselName, mmsi, imo, nationalities, vesselTypeCode);
    }
}
