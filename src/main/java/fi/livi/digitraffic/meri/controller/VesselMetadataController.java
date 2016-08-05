package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_METADATA_PART_PATH;
import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.domain.VesselMetadata;
import fi.livi.digitraffic.meri.service.VesselMetadataService;
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

    @ApiOperation("Return vessel metadata by mmsi.")
    @RequestMapping(method = RequestMethod.GET, path = VESSELS_PATH + "/{mmsi}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public VesselMetadata vesselMetadataByMssi(@PathVariable("mmsi") final int mmsi) {
        return vesselMetadataService.findMetadataByMssi(mmsi);
    }

    @ApiOperation("Return vessel metadata for all known vessels.")
    @RequestMapping(method = RequestMethod.GET, path = VESSELS_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<VesselMetadata> allVessels() {
        return vesselMetadataService.listAllVesselMetadata();
    }
}
