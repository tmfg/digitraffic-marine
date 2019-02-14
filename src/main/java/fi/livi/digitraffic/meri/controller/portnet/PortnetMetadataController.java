package fi.livi.digitraffic.meri.controller.portnet;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_METADATA_PART_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
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
import fi.livi.digitraffic.meri.model.portnet.metadata.CodeDescriptions;
import fi.livi.digitraffic.meri.model.portnet.metadata.FeatureCollectionList;
import fi.livi.digitraffic.meri.service.portnet.PortnetMetadataService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(API_V1_BASE_PATH + API_METADATA_PART_PATH)
@ConditionalOnWebApplication
public class PortnetMetadataController {
    public static final String CODE_DESCRIPTIONS = "/code-descriptions";
    public static final String SSN_LOCATIONS_PATH =  "/locations";
    public static final String SSN_LOCATIONS_BY_COUNTRY_PATH =  "/locations-by-country";
    public static final String VESSEL_DETAILS_PATH = "/vessel-details";

    private final PortnetMetadataService portnetMetadataService;

    public PortnetMetadataController(final PortnetMetadataService portnetMetadataService) {
        this.portnetMetadataService = portnetMetadataService;
    }

    @ApiOperation("Return all code descriptions.")
    @GetMapping(path = CODE_DESCRIPTIONS, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public CodeDescriptions listCodeDescriptions() {
        return portnetMetadataService.listCodeDescriptions();
    }

    @ApiOperation("Return list of all berths, port areas and locations.")
    @GetMapping(path = SSN_LOCATIONS_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public FeatureCollectionList listAllMetadata() {
        return portnetMetadataService.listaAllMetadata();
    }

    @ApiOperation("Return one location's berths, port areas and location by SafeSeaNet location code.")
    @GetMapping(path = SSN_LOCATIONS_PATH + "/{locode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful retrieval of ssn location"),
                    @ApiResponse(code = 404, message = "Ssn location not found"),
                    @ApiResponse(code = 500, message = "Internal server error") })
    @ResponseBody
    public FeatureCollectionList findSsnLocationByLocode(@PathVariable(value = "locode", required = true) final String locode) {
        return portnetMetadataService.findSsnLocationByLocode(locode);
    }

    @ApiOperation("Return list of SafeSeaNet locations by country name")
    @GetMapping(path = SSN_LOCATIONS_BY_COUNTRY_PATH + "/{country}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public FeatureCollectionList findSsnLocationsByCountry(@PathVariable(value = "country", required = true) final String country) {
        return portnetMetadataService.findSsnLocationsByCountry(country);
    }

    @ApiOperation("Return list of vessels details")
    @GetMapping(path = VESSEL_DETAILS_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful retrieval of vessel details"),
                    @ApiResponse(code = 500, message = "Internal server error") })
    @ResponseBody
    public List<VesselDetails> findVesselDetails(
            @ApiParam("Return details of vessels whose metadata has changed after given time in ISO date format {yyyy-MM-dd'T'HH:mm:ss.SSSZ} e.g. 2016-10-31T06:30:00.000Z. " +
                      "Default value is now minus 24 hours if all parameters are empty.")
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DATE_TIME) ZonedDateTime from,

            @ApiParam("Return vessel details for given vessel name")
            @RequestParam(value = "vesselName", required = false) final String vesselName,

            @ApiParam("Return vessel details for given mmsi")
            @RequestParam(value = "mmsi", required = false) final Integer mmsi,

            @ApiParam("Return vessel details for given IMO/LLOYDS")
            @RequestParam(value = "imo", required = false) final Integer imo,

            @ApiParam("Return vessel details for vessels with given nationality or nationalities (e.g. FI)")
            @RequestParam(value = "nationality", required = false) final List<String> nationalities,

            @ApiParam("Return vessel details for given vessel type code")
            @RequestParam(value = "vesselTypeCode", required = false) final Integer vesselTypeCode) {

        if (!ObjectUtils.anyNotNull(from, vesselName, mmsi, imo, nationalities, vesselTypeCode)) {
            from = ZonedDateTime.now().minusDays(1L);
        }

        return portnetMetadataService.findVesselDetails(from, vesselName, mmsi, imo, nationalities, vesselTypeCode);
    }
}
