package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_LOCATIONS_PATH;
import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.model.ais.VesselLocationFeatureCollection;
import fi.livi.digitraffic.meri.service.ais.VesselLocationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(API_V1_BASE_PATH + API_LOCATIONS_PATH)
public class VesselLocationController {
    private static final Logger LOG = Logger.getLogger(VesselLocationController.class);

    private final VesselLocationService vesselLocationService;

    public static final String LATEST_PATH =  "/latest";
    public static final String BY_POINT_PATH =  "/by-point";
    public static final String BY_MMSI_PATH =  "/by-mmsi";

    @Autowired
    public VesselLocationController(final VesselLocationService vesselLocationService) {
        this.vesselLocationService = vesselLocationService;
    }

    @ApiOperation("Find latest vessel locations by mmsi and optional timestamp interval in milliseconds from Unix epoch.")
    @GetMapping(path = LATEST_PATH + "/{mmsi}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public VesselLocationFeatureCollection vesselLocationsByMssiAndTimestamp(
            @ApiParam(value = "Maritime Mobile Service Identity (MMSI)", required = true)
            @PathVariable("mmsi")
            final int mmsi,
            @ApiParam("From timestamp timestamp in milliseconds from Unix epoch 1970-01-01T00:00:00Z")
            @RequestParam(value = "from", required = false)
            final Long from,
            @ApiParam("To timestamp")
            @RequestParam(value = "to", required = false)
            final Long to) {

        LOG.info(String.format("vesselLocationsByMssiAndTimestamp mmsi:\t%d from:\t%d to:\t%d", mmsi, from, to));

        return vesselLocationService.findAllowedLocations(mmsi, from, to);
    }

    @ApiOperation("Find latest vessel locations by timestamp interval in milliseconds from Unix epoch.")
    @GetMapping(path = LATEST_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public VesselLocationFeatureCollection vesselLocationsByTimestamp(
            @ApiParam("From timestamp timestamp in milliseconds from Unix epoch 1970-01-01T00:00:00Z")
            @RequestParam(value = "from", required = false)
            final Long from,
            @ApiParam("To timestamp")
            @RequestParam(value = "to", required = false)
            final Long to) {

        LOG.info(String.format("vesselLocationsByTimestamp from:\t%d to:\t%d", from, to));

        return vesselLocationService.findAllowedLocations(from, to);
    }

    @ApiOperation("Find vessel locations within a circle surrounding a point. " +
                  "(CAUTION: Data is not reliable because it is missing some vessels such as fishing boats, boats with the AIS system turned off, " +
                  "boats without the AIS system, vessels outside of the AIS range and so on.)")
    @GetMapping(path = BY_POINT_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public VesselLocationFeatureCollection vesselLocationsWithingRadiusFromPoint(
            @ApiParam("Radius of search circle in kilometers (km) using haversine formula.")
            @RequestParam(value = "radius", required = true)
            final double radius,
            @ApiParam("Latitude of the point")
            @RequestParam(value = "latitude", required = true)
            final double latitude,
            @ApiParam("Longitude of the point")
            @RequestParam(value = "longitude", required = true)
            final double longitude,
            @ApiParam("From timestamp in milliseconds from Unix epoch 1970-01-01T00:00:00Z")
            @RequestParam(value = "from", required = true)
            final long from) {

        return vesselLocationService.findAllowedLocationsWithinRadiusFromPoint(radius, latitude, longitude, from);
    }

    @ApiOperation("Find vessel locations within a circle surrounding a vessel. " +
                  "(CAUTION: Data is not reliable because it is missing some vessels such as fishing boats, boats with the AIS system turned off, " +
                  "boats without the AIS system, vessels outside of the AIS range and so on.)")
    @GetMapping(path = BY_MMSI_PATH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public VesselLocationFeatureCollection vesselLocationsWithingRadiusFromMMSI(
            @ApiParam("Radius of search circle in kilometers (km) using haversine formula.")
            @RequestParam(value = "radius", required = true)
            final double radius,
            @ApiParam("MMSI of the vessel")
            @RequestParam(value = "mmsi", required = true)
            final int mmsi,
            @ApiParam("From timestamp in milliseconds from Unix epoch 1970-01-01T00:00:00Z")
            @RequestParam(value = "from", required = true)
            final long from) {

        return vesselLocationService.findAllowedLocationsWithinRadiusFromMMSI(radius, mmsi, from);
    }
}
