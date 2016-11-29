package fi.livi.digitraffic.meri.controller.portnet;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_PORT_CALLS_PATH;
import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.model.portnet.data.PortCallsJson;
import fi.livi.digitraffic.meri.service.BadRequestException;
import fi.livi.digitraffic.meri.service.portnet.PortCallService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(API_V1_BASE_PATH + API_PORT_CALLS_PATH)
public class PortCallController {
    private final PortCallService portCallService;

    public PortCallController(final PortCallService portCallService) {
        this.portCallService = portCallService;
    }

    @ApiOperation("Find port calls")
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful retrieval of port calls"),
                    @ApiResponse(code = 400, message = "Bad request. At least one parameter is required."),
                    @ApiResponse(code = 500, message = "Internal server error") })
    @ResponseBody
    public PortCallsJson listAllPortCalls(
            @ApiParam("Return port calls from given port")
            @RequestParam(value = "locode", required = false) final String locode,

            @ApiParam("Return port calls received on given date in ISO date format {yyyy-MM-dd} e.g. 2016-10-31.")
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DATE) final Date date,

            @ApiParam("Return port calls received after given time in ISO date format {yyyy-MM-dd'T'HH:mm:ss.SSSZ} e.g. 2016-10-31T06:30:00.000Z.")
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DATE_TIME) final ZonedDateTime from,

            @ApiParam("Return port calls from given vessel")
            @RequestParam(value = "vesselName", required = false) final String vesselName) {

        if (locode == null && date == null && from == null && vesselName == null) {
            throw new BadRequestException("At least one parameter is required");
        }

        return portCallService.findPortCalls(date, from, locode, vesselName);
    }
}
