package fi.livi.digitraffic.meri.controller.portnet;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_METADATA_PART_PATH;
import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.model.portnet.metadata.PortsAndBerthsJson;
import fi.livi.digitraffic.meri.service.portnet.PortnetMetadataService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(API_V1_BASE_PATH + API_METADATA_PART_PATH)
public class PortnetMetadataController {
    public static final String SSN_PATH =  "/ssn";

    private final PortnetMetadataService portnetMetadataService;

    public PortnetMetadataController(final PortnetMetadataService portnetMetadataService) {
        this.portnetMetadataService = portnetMetadataService;
    }

    @ApiOperation("Return list of all berths, port areas and locations.")
    @GetMapping(path = SSN_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public PortsAndBerthsJson listAllMetadata() {
        return portnetMetadataService.listaAllMetadata();
    }

    @ApiOperation("Return one locations by locode.")
    @GetMapping(path = SSN_PATH + "/{locode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public PortsAndBerthsJson findSsnLocationByLocode(@PathVariable("locode") final String locode) {
        return portnetMetadataService.findSsnLocationByLocode(locode);
    }
}
