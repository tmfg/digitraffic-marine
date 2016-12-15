package fi.livi.digitraffic.meri.controller.portnet;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_METADATA_PART_PATH;
import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.model.portnet.metadata.PortsAndBerthsJson;
import fi.livi.digitraffic.meri.model.portnet.metadata.SsnLocationJson;
import fi.livi.digitraffic.meri.service.portnet.PortnetMetadataService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(API_V1_BASE_PATH + API_METADATA_PART_PATH)
public class PortnetMetadataController {
    public static final String SSN_LOCATIONS_PATH =  "/locations";
    public static final String SSN_LOCATIONS_BY_COUNTRY_PATH =  "/locations-by-country";

    private final PortnetMetadataService portnetMetadataService;

    public PortnetMetadataController(final PortnetMetadataService portnetMetadataService) {
        this.portnetMetadataService = portnetMetadataService;
    }

    @ApiOperation("Return list of all berths, port areas and locations.")
    @GetMapping(path = SSN_LOCATIONS_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public PortsAndBerthsJson listAllMetadata() {
        return portnetMetadataService.listaAllMetadata();
    }

    @ApiOperation("Return one location's berths, port areas and location by SafeSeaNet location code.")
    @GetMapping(path = SSN_LOCATIONS_PATH + "/{locode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful retrieval of ssn location"),
                    @ApiResponse(code = 404, message = "Ssn location not found"),
                    @ApiResponse(code = 500, message = "Internal server error") })
    @ResponseBody
    public PortsAndBerthsJson findSsnLocationByLocode(@PathVariable(value = "locode", required = true) final String locode) {
        return portnetMetadataService.findSsnLocationByLocode(locode);
    }

    @ApiOperation("Return list of SafeSeaNet locations by country name")
    @GetMapping(path = SSN_LOCATIONS_BY_COUNTRY_PATH + "/{country}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<SsnLocationJson> findSsnLocationsByCountry(@PathVariable(value = "country", required = true) final String country) {
        return portnetMetadataService.findSsnLocationsByCountry(country);
    }
}
