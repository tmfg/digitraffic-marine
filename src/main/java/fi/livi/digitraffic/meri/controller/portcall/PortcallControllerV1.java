package fi.livi.digitraffic.meri.controller.portcall;

import fi.livi.digitraffic.meri.controller.MediaTypes;
import fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallsJson;
import fi.livi.digitraffic.meri.model.portnet.metadata.CodeDescriptions;
import fi.livi.digitraffic.meri.model.portnet.metadata.LocationFeatureCollections;
import fi.livi.digitraffic.meri.service.portnet.PortCallService;
import fi.livi.digitraffic.meri.service.portnet.PortnetMetadataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import static fi.livi.digitraffic.meri.controller.ApiConstants.*;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.*;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Tag(name = PORT_CALL_BETA_TAG, description = "Port Call Controller")
@RestController
@Validated
@ConditionalOnWebApplication
public class PortcallControllerV1 {
    private static final String NOTE = "If the search result size exceeds 1000 items, the operation will return an error. " +
        "In this case you should try to narrow down your search criteria.\n\n" +
        "All dates/times are in ISO 8601 format, e.g. 2016-10-31 or 2016-10-31T06:30:00.000Z";

    private static final String API_PORT_CALL_BETA = API_PORT_CALL + BETA;

    private final PortCallService portCallService;

    private final PortnetMetadataService portnetMetadataService;

    public PortcallControllerV1(final PortCallService portCallService,
                                final PortnetMetadataService portnetMetadataService) {
        this.portCallService = portCallService;
        this.portnetMetadataService = portnetMetadataService;
    }

    @Operation(summary = "Find port calls", description = NOTE)
    @GetMapping(path = API_PORT_CALL_BETA + "/port-calls", produces = MediaTypes.MEDIA_TYPE_APPLICATION_JSON)
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of port calls"),
        @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public PortCallsJson listAllPortCalls(
        @Parameter(description = "Return port calls received on given date.")
        @RequestParam(value = "date", required = false)
        @DateTimeFormat(iso = DATE) final Date date,

        @Parameter(description = "Return port calls received after given time. " +
            "Default value is now minus 24 hours if all parameters are empty.")
        @RequestParam(value = "from", required = false)
        @DateTimeFormat(iso = DATE_TIME) ZonedDateTime from,

        @Parameter(description = "Return port calls whose ETA time is after the given time")
        @RequestParam(value = "etaFrom", required = false)
        @DateTimeFormat(iso = DATE_TIME) ZonedDateTime etaFrom,

        @Parameter(description = "Return port calls whose ETD time is after the given time")
        @RequestParam(value = "etdFrom", required = false)
        @DateTimeFormat(iso = DATE_TIME) ZonedDateTime etdFrom,

        @Parameter(description = "Return port calls whose ATA time is after the given time")
        @RequestParam(value = "ataFrom", required = false)
        @DateTimeFormat(iso = DATE_TIME) ZonedDateTime ataFrom,

        @Parameter(description = "Return port calls whose ATD time is after the given time")
        @RequestParam(value = "atdFrom", required = false)
        @DateTimeFormat(iso = DATE_TIME) ZonedDateTime atdFrom,

        @Parameter(description = "Return port calls whose ETA time is before the given time")
        @RequestParam(value = "etaTo", required = false)
        @DateTimeFormat(iso = DATE_TIME) ZonedDateTime etaTo,

        @Parameter(description = "Return port calls whose ETD time is before the given time")
        @RequestParam(value = "etdTo", required = false)
        @DateTimeFormat(iso = DATE_TIME) ZonedDateTime etdTo,

        @Parameter(description = "Return port calls whose ATA time is before the given time")
        @RequestParam(value = "ataTo", required = false)
        @DateTimeFormat(iso = DATE_TIME) ZonedDateTime ataTo,

        @Parameter(description = "Return port calls whose ATD time is before the given time")
        @RequestParam(value = "atdTo", required = false)
        @DateTimeFormat(iso = DATE_TIME) ZonedDateTime atdTo,

        @Parameter(description = "Return port calls for given vessel name")
        @RequestParam(value = "vesselName", required = false) final String vesselName,

        @Parameter(description = "Return port calls for given mmsi")
        @RequestParam(value = "mmsi", required = false) final Integer mmsi,

        @Parameter(description = "Return port calls for given IMO/LLOYDS")
        @RequestParam(value = "imo", required = false) final Integer imo,

        @Parameter(description = "Return port calls for vessels with given nationality")
        @RequestParam(value = "nationality", required = false) final List<String> nationality,

        @Parameter(description = "Return port calls for given vessel type code")
        @RequestParam(value = "vesselTypeCode", required = false) final Integer vesselTypeCode
    ) {

        if(!ObjectUtils.anyNotNull(date, from, vesselName, mmsi, imo, nationality, vesselTypeCode)) {
            from = ZonedDateTime.now().minusDays(1);
        }

        return portCallService.findPortCallsWithTimestamps(date,
            from,
            etaFrom,
            etaTo,
            etdFrom,
            etdTo,
            ataFrom,
            ataTo,
            atdFrom,
            atdTo,
            vesselName,
            mmsi,
            imo,
            nationality,
            vesselTypeCode);
    }

    @Operation(summary = "Return all code descriptions")
    @GetMapping(path = API_PORT_CALL_BETA + "/code-descriptions", produces = MediaTypes.MEDIA_TYPE_APPLICATION_JSON)
    @ResponseBody
    public CodeDescriptions listCodeDescriptions() {
        return portnetMetadataService.listCodeDescriptions();
    }

    @Operation(summary = "Return list of all berths, port areas and locations.")
    @GetMapping(path = API_PORT_CALL_BETA + "/locations", produces = MediaTypes.MEDIA_TYPE_APPLICATION_JSON)
    @ResponseBody
    public LocationFeatureCollections listAllMetadata() {
        return portnetMetadataService.listaAllMetadata();
    }

    @Operation(summary = "Return one location's berths, port areas and location by SafeSeaNet location code.")
    @GetMapping(path = API_PORT_CALL_BETA + "/locations/{locode}", produces = MediaTypes.MEDIA_TYPE_APPLICATION_JSON)
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of ssn location"),
        @ApiResponse(responseCode = HTTP_NOT_FOUND, description = "Ssn location not found", content = @Content),
        @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public LocationFeatureCollections findSsnLocationByLocode(@PathVariable(value = "locode") final String locode) {
        return portnetMetadataService.findSsnLocationByLocode(locode);
    }

    @Operation(summary = "Return list of vessels details.")
    @GetMapping(path = API_PORT_CALL_BETA + "/vessel-details", produces = MediaTypes.MEDIA_TYPE_APPLICATION_JSON)
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
