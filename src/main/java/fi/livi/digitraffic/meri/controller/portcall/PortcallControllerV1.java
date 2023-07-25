package fi.livi.digitraffic.meri.controller.portcall;

import static fi.livi.digitraffic.meri.controller.ApiConstants.API_PORT_CALL;
import static fi.livi.digitraffic.meri.controller.ApiConstants.BETA;
import static fi.livi.digitraffic.meri.controller.ApiConstants.PORT_CALL_V1_TAG;
import static fi.livi.digitraffic.meri.controller.ApiConstants.V1;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.HTTP_INTERNAL_SERVER_ERROR;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.HTTP_NOT_FOUND;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.HTTP_OK;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_JSON;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.controller.ResponseEntityWithLastModifiedHeader;
import fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails;
import fi.livi.digitraffic.meri.dto.portcall.v1.CodeDescriptionsV1;
import fi.livi.digitraffic.meri.dto.portcall.v1.PortCallsV1;
import fi.livi.digitraffic.meri.dto.portcall.v1.PortLocationDtoV1;
import fi.livi.digitraffic.meri.service.portcall.PortCallServiceV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = PORT_CALL_V1_TAG, description = "Port Call APIs")
@RestController
@Validated
@ConditionalOnWebApplication
public class PortcallControllerV1 {
    private static final String NOTE = "If the search result size exceeds 1000 items, the operation will return an error, and thus " +
        "try to narrow down your search criteria to limit the result size below 1000 items.\n\n" +
        "Data is provided as is and isn't moderated in any way. " +
        "Timestamps are in valid ISO 8601 format, e.g. 2016-10-31 or 2016-10-31T06:30:00.000Z, but may still contain anomalies such as the timestamp being in distant future.";

    public static final String API_PORT_CALL_V1 = API_PORT_CALL + V1;
    public static final String PORT_CALLS = "/port-calls";
    public static final String CODE_DESCRIPTIONS = "/code-descriptions";
    public static final String VESSEL_DETAILS = "/vessel-details";
    public static final String PORTS = "/ports";

    private final PortCallServiceV1 portCallServiceV1;

    public PortcallControllerV1(final PortCallServiceV1 portCallServiceV1) {
        this.portCallServiceV1 = portCallServiceV1;
    }

    @Operation(summary = "Find port calls", description = NOTE)
    @GetMapping(path = API_PORT_CALL_V1 + PORT_CALLS, produces = MEDIA_TYPE_APPLICATION_JSON)
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of port calls"),
        @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public PortCallsV1 listAllPortCalls(
        @Parameter(description = "Return port calls received on given date.")
        @RequestParam(value = "date", required = false)
        @DateTimeFormat(iso = DATE) final Date date,

        @Parameter(description = "Return port calls received after given time. " +
            "Default value is now minus 24 hours if all parameters are empty.")
        @RequestParam(value = "from", required = false)
        @DateTimeFormat(iso = DATE_TIME) final Instant from,

        @Parameter(description = "Return port calls received before given time." +
            "Default value is now plus 100 days, if parameter is empty.")
        @RequestParam(value = "to", required = false)
        @DateTimeFormat(iso = DATE_TIME) final Instant to,

        @Parameter(description = "Return port calls whose ETA time is after the given time")
        @RequestParam(value = "etaFrom", required = false)
        @DateTimeFormat(iso = DATE_TIME) final Instant etaFrom,

        @Parameter(description = "Return port calls whose ETD time is after the given time")
        @RequestParam(value = "etdFrom", required = false)
        @DateTimeFormat(iso = DATE_TIME) final Instant etdFrom,

        @Parameter(description = "Return port calls whose ATA time is after the given time")
        @RequestParam(value = "ataFrom", required = false)
        @DateTimeFormat(iso = DATE_TIME) final Instant ataFrom,

        @Parameter(description = "Return port calls whose ATD time is after the given time")
        @RequestParam(value = "atdFrom", required = false)
        @DateTimeFormat(iso = DATE_TIME) final Instant atdFrom,

        @Parameter(description = "Return port calls whose ETA time is before the given time")
        @RequestParam(value = "etaTo", required = false)
        @DateTimeFormat(iso = DATE_TIME) final Instant etaTo,

        @Parameter(description = "Return port calls whose ETD time is before the given time")
        @RequestParam(value = "etdTo", required = false)
        @DateTimeFormat(iso = DATE_TIME) final Instant etdTo,

        @Parameter(description = "Return port calls whose ATA time is before the given time")
        @RequestParam(value = "ataTo", required = false)
        @DateTimeFormat(iso = DATE_TIME) final Instant ataTo,

        @Parameter(description = "Return port calls whose ATD time is before the given time")
        @RequestParam(value = "atdTo", required = false)
        @DateTimeFormat(iso = DATE_TIME) final Instant atdTo,

        @Parameter(description = "Return port calls for given locode")
        @RequestParam(value = "locode", required = false) final String locode,

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

        // use default, if no other parameters given
        final Instant actualFrom;
        if (!ObjectUtils.anyNotNull(date, from, to, ataFrom, ataTo, atdFrom, atdTo, etaFrom, etaTo, etdFrom, etdTo, vesselName, mmsi, imo, nationality, vesselTypeCode)) {
            actualFrom = Instant.now().minus(Duration.ofDays(1));
        } else {
            actualFrom = from;
        }

        final Instant actualTo = to != null ? to : Instant.now().plus(Duration.ofDays(100));

        return portCallServiceV1.findPortCalls(date,
            actualFrom,
            actualTo,
            etaFrom,
            etaTo,
            etdFrom,
            etdTo,
            ataFrom,
            ataTo,
            atdFrom,
            atdTo,
            locode,
            vesselName,
            mmsi,
            imo,
            nationality,
            vesselTypeCode);
    }

    @Operation(summary = "Return all code descriptions")
    @GetMapping(path = API_PORT_CALL_V1 + CODE_DESCRIPTIONS, produces = MEDIA_TYPE_APPLICATION_JSON)
    @ResponseBody
    public CodeDescriptionsV1 listCodeDescriptions() {
        return portCallServiceV1.listCodeDescriptions();
    }

    @Operation(summary = "Return all ports with port areas and berths.")
    @GetMapping(path = API_PORT_CALL_V1 + PORTS, produces = MEDIA_TYPE_APPLICATION_JSON)
    @ResponseBody
    public PortLocationDtoV1 findPortsLocations() {
        return portCallServiceV1.findPortsLocations();
    }

    @Operation(summary = "Return one port with port areas and berths by SafeSeaNet location code (locode).")
    @GetMapping(path = API_PORT_CALL_V1 + PORTS + "/{locode}", produces = MEDIA_TYPE_APPLICATION_JSON)
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of ports"),
                    @ApiResponse(responseCode = HTTP_NOT_FOUND, description = "Location not found", content = @Content),
                    @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public PortLocationDtoV1 findPortLocationByLocode(@PathVariable(value = "locode") final String locode) {
        return portCallServiceV1.findPortLocationByLocode(locode);
    }


    @Operation(summary = "Return list of vessels details.")
    @GetMapping(path = API_PORT_CALL_V1 + VESSEL_DETAILS, produces = MEDIA_TYPE_APPLICATION_JSON)
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of vessel details"),
        @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public ResponseEntityWithLastModifiedHeader<List<VesselDetails>> findVesselDetails(
        @Parameter(description = "Return details of vessels whose metadata has changed after given time in ISO date format {yyyy-MM-dd'T'HH:mm:ss.SSSZ} e.g. 2016-10-31T06:30:00.000Z. " +
            "Default value is now minus 24 hours if all parameters are empty.")
        @RequestParam(value = "from", required = false)
        @DateTimeFormat(iso = DATE_TIME) Instant from,

        @Parameter(description = "Return vessel details for given vessel name")
        @RequestParam(value = "vesselName", required = false) final String vesselName,

        @Parameter(description = "Return vessel details for given mmsi")
        @RequestParam(value = "mmsi", required = false) final Integer mmsi,

        @Parameter(description = "Return vessel details for given IMO/LLOYDS")
        @RequestParam(value = "imo", required = false) final Integer imo,

        @Parameter(description = "Return vessel details for vessels with given nationality or nationalities (e.g. FI)")
        @RequestParam(value = "nationality", required = false) final List<String> nationality,

        @Parameter(description = "Return vessel details for given vessel type code")
        @RequestParam(value = "vesselTypeCode", required = false) final Integer vesselTypeCode) {

        if (!ObjectUtils.anyNotNull(from, vesselName, mmsi, imo, nationality, vesselTypeCode)) {
            from = Instant.now().minus(Duration.ofDays(1));
        }

        final List<VesselDetails> vds = portCallServiceV1.findVesselDetails(from, vesselName, mmsi, imo, nationality, vesselTypeCode);
        final Instant lastModified = vds.stream()
            .map(VesselDetails::getLastModified)
            .filter(ObjectUtils::isNotEmpty)
            .max(Comparator.comparing(Function.identity())).orElse(Instant.EPOCH);
        return ResponseEntityWithLastModifiedHeader.of(vds, lastModified, API_PORT_CALL_V1 + VESSEL_DETAILS);

    }
}
