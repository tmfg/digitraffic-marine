package fi.livi.digitraffic.meri.controller.portnet;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_PORT_CALLS_PATH;
import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

import java.time.ZonedDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.model.portnet.data.PortCallsJson;
import fi.livi.digitraffic.meri.service.portnet.PortCallService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(API_V1_BASE_PATH + API_PORT_CALLS_PATH)
public class PortcallController {
    private final PortCallService portCallService;

    public PortcallController(final PortCallService portCallService) {
        this.portCallService = portCallService;
    }

    @ApiOperation("Return list of all port calls.")
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public PortCallsJson listAllPortCalls(
            @ApiParam("Return port calls from given port")
            @RequestParam(value = "locode", required = false)final String locode,
            @ApiParam("Return port calls received after given timestamp")
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DATE_TIME)
            final ZonedDateTime from) {
        return portCallService.listAllPortCalls(locode, from);
    }
}
