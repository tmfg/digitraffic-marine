package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_METADATA_PART_PATH;
import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.model.portnet.PortsAndBerthsJson;
import fi.livi.digitraffic.meri.service.SsnService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(API_V1_BASE_PATH + API_METADATA_PART_PATH)
public class SsnMetadataController {
    public static final String SSN_PATH =  "/ssn";

    private final SsnService ssnService;

    public SsnMetadataController(final SsnService ssnService) {
        this.ssnService = ssnService;
    }

    @ApiOperation("Return list of all berths, port areas and locations.")
    @GetMapping(path = SSN_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public PortsAndBerthsJson listAllBerths() {
        return ssnService.listaAllMetadata();
    }

}
