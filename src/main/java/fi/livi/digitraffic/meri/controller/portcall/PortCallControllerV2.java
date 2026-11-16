package fi.livi.digitraffic.meri.controller.portcall;

import fi.livi.digitraffic.meri.controller.CacheControl;
import fi.livi.digitraffic.meri.dto.portcall.v1.port.PortLocationDtoV1;
import fi.livi.digitraffic.meri.service.portcall.v2.PortCallWebServiceV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static fi.livi.digitraffic.meri.controller.ApiConstants.*;
import static fi.livi.digitraffic.meri.controller.HttpCodeConstants.*;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_JSON;

@Tag(name = PORT_CALL_V2_TAG, description = "Port Call APIs")
@RestController
@Validated
@ConditionalOnWebApplication
public class PortCallControllerV2 {
    public static final String API_PORT_CALL_V2 = API_PORT_CALL + V2;
    public static final String PORTS = "/ports";

    private final PortCallWebServiceV2 portCallWebServiceV2;

    public PortCallControllerV2(final PortCallWebServiceV2 portCallWebServiceV2) {
        this.portCallWebServiceV2 = portCallWebServiceV2;
    }

    @Operation(summary = "Return all ports with port areas and berths.")
    @GetMapping(path = API_PORT_CALL_V2 + PORTS, produces = MEDIA_TYPE_APPLICATION_JSON)
    @ResponseBody
    public PortLocationDtoV1 findPortsLocations(final HttpServletResponse response,
            @Parameter(description = "Return ports for given country(2-letter ISO country code).")
            @RequestParam(value = "country", required = false)
            final String country) {
        CacheControl.setOneDayCache(response);

        if(country != null) {
            if(country.length() != 2) {
                throw new IllegalArgumentException("Country must be a 2-letter ISO country code.");
            }

            return portCallWebServiceV2.findPortsLocations(country.toUpperCase(Locale.ROOT));
        }

        return portCallWebServiceV2.findPortsLocations();
    }

    @Operation(summary = "Return one port with port areas and berths by SafeSeaNet location code (locode)")
    @GetMapping(path = API_PORT_CALL_V2 + PORTS + "/{locode}", produces = MEDIA_TYPE_APPLICATION_JSON)
    @ApiResponses({ @ApiResponse(responseCode = HTTP_OK, description = "Successful retrieval of ports"),
        @ApiResponse(responseCode = HTTP_NOT_FOUND, description = "Location not found", content = @Content),
        @ApiResponse(responseCode = HTTP_INTERNAL_SERVER_ERROR, description = "Internal server error", content = @Content) })
    @ResponseBody
    public PortLocationDtoV1 findPortLocationByLocode(@PathVariable(value = "locode") final String locode,
                                                      final HttpServletResponse response) {
        final var responseDto = portCallWebServiceV2.findPortLocationByLocode(locode);

        // set cache after find, it can throw ObjectNotFoundException
        CacheControl.setOneDayCache(response);

        return responseDto;
    }
}
