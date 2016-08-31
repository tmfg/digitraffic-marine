package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;

import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.domain.VesselLocation;
import fi.livi.digitraffic.meri.service.VesselLocationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(API_V1_BASE_PATH + "/locations")
public class VesselLocationController {
    private static final Logger LOG = Logger.getLogger(VesselLocationController.class);

    private final VesselLocationService vesselLocationService;

    public static final String LATEST_PATH =  "/latest";

    @Autowired
    public VesselLocationController(final VesselLocationService vesselLocationService) {
        this.vesselLocationService = vesselLocationService;
    }

    @ApiOperation("Find latest vessel locations by mmsi and optional timestamp interval in milliseconds from Unix epoch.")
    @GetMapping(path = LATEST_PATH + "/{mmsi}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<VesselLocation> vesselLocationsByMssiAndTimestamp(
            @ApiParam(value = "Maritime Mobile Service Identity (MMSI)", required = true)
            @PathVariable("mmsi")
            final int mmsi,
            @ApiParam("From timestamp timestamp in milliseconds from Unix epoch 1970-01-01T00:00:00Z")
            @RequestParam(value = "from", required = false)
            final Long from,
            @ApiParam("To timestamp")
            @RequestParam(value = "to", required = false)
            final Long to) {

        LOG.info("vesselLocationsByMssiAndTimestamp mmsi:\t" + mmsi + " from:\t" + from + " to:\t" + to);
        return IteratorUtils.toList( vesselLocationService.findLocations(mmsi, from, to).iterator() );
    }

    @ApiOperation("Find latest vessel locations by timestamp interval in milliseconds from Unix epoch.")
    @GetMapping(path = LATEST_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<VesselLocation> vesselLocationsByTimestamp(
            @ApiParam("From timestamp timestamp in milliseconds from Unix epoch 1970-01-01T00:00:00Z")
            @RequestParam(value = "from", required = false)
            final Long from,
            @ApiParam("To timestamp")
            @RequestParam(value = "to", required = false)
            final Long to) {

        LOG.info("vesselLocationsByTimestamp from:\t" + from + " to:\t" + to);
        return IteratorUtils.toList( vesselLocationService.findLocations(from, to).iterator() );
    }
}
