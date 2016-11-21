package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_METADATA_PART_PATH;
import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.model.ais.VesselMetadataJson;
import fi.livi.digitraffic.meri.service.ais.VesselMetadataService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(API_V1_BASE_PATH + API_METADATA_PART_PATH)
public class VesselMetadataController {
    private final VesselMetadataService vesselMetadataService;

    public static final String VESSELS_PATH =  "/vessels";

    @Autowired
    public VesselMetadataController(final VesselMetadataService vesselMetadataService) {
        this.vesselMetadataService = vesselMetadataService;
    }

    @ApiOperation("Return latest vessel metadata by mmsi.")
    @GetMapping(path = VESSELS_PATH + "/{mmsi}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public VesselMetadataJson vesselMetadataByMssi(@PathVariable("mmsi") final int mmsi) {
        return vesselMetadataService.findMetadataByMssi(mmsi);
    }

    @ApiOperation("Return latest vessel metadata for all known vessels.")
    @GetMapping(path = VESSELS_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<VesselMetadataJson> allVessels() {
        return vesselMetadataService.listAllVesselMetadata();
    }
}
