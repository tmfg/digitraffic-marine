package fi.livi.digitraffic.meri.controller.portnet;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_PORT_CALLS_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

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
import fi.livi.digitraffic.meri.model.portnet.data.PortCallsJson;
import fi.livi.digitraffic.meri.service.portnet.PortCallService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(API_V1_BASE_PATH + API_PORT_CALLS_PATH)
@ConditionalOnWebApplication
@Tag(name="port-call-controller", description = "Port Call Controller")
public class PortCallController {
    private final PortCallService portCallService;

    private static final String NOTE = "If the search result size exceeds 1000 items, the operation will return an error. " +
                                       "In this case you should try to narrow down your search criteria.\n\n" +
                                       "All dates/times are in ISO 8601 format, e.g. 2016-10-31 or 2016-10-31T06:30:00.000Z";

    public PortCallController(final PortCallService portCallService) {
        this.portCallService = portCallService;
    }

    @Operation(summary = "Find port calls", description = NOTE)
    @GetMapping(produces = MediaTypes.MEDIA_TYPE_APPLICATION_JSON)
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successful retrieval of port calls"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
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

    @Operation(summary = "Find port calls", description = NOTE)
    @GetMapping(path = "/{locode}", produces = MediaTypes.MEDIA_TYPE_APPLICATION_JSON)
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successful retrieval of port calls"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
    @ResponseBody
    public PortCallsJson listAllPortCallsFromLocode(
            @Parameter(description = "Return port calls from given port", required = true)
            @PathVariable("locode") final String locode,

            @Parameter(description = "Return port calls received on given date.")
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DATE) final Date date,

            @Parameter(description = "Return port calls received after given time.")
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DATE_TIME) final ZonedDateTime from,

            @Parameter(description = "Return port calls received before given time.")
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DATE_TIME) final ZonedDateTime to,

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
        return portCallService.findPortCalls(date,
            from,
            to,
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

    @Operation(summary = "Find port calls", description = NOTE)
    @GetMapping(path = "/from/{from}/to/{to}", produces = MediaTypes.MEDIA_TYPE_APPLICATION_JSON)
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Successful retrieval of port calls"),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
    @ResponseBody
    public PortCallsJson listAllPortCallsFromTo(
        @Parameter(description = "Return port calls received after given time.",
                  required = true)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @PathVariable("from") final ZonedDateTime from,

        @Parameter(description = "Return port calls received before given time.",
                  required = true)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @PathVariable("to") final ZonedDateTime to,

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
        return portCallService.findPortCallsWithoutTimestamps(from,
            to,
            vesselName,
            mmsi,
            imo,
            nationality,
            vesselTypeCode);
    }
}
