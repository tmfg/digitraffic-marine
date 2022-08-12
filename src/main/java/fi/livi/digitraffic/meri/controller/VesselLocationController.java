package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_LOCATIONS_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_JSON;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.model.ais.VesselLocationFeatureCollection;
import fi.livi.digitraffic.meri.service.ais.VesselLocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(API_V1_BASE_PATH + API_LOCATIONS_PATH)
@ConditionalOnWebApplication
@Tag(name = "vessel-location-controller", description = "Vessel Location Controller")
public class VesselLocationController {
    private static final Logger LOG = LoggerFactory.getLogger(VesselLocationController.class);

    private final VesselLocationService vesselLocationService;

    public static final String LATEST_PATH =  "/latest";

    @Autowired
    public VesselLocationController(final VesselLocationService vesselLocationService) {
        this.vesselLocationService = vesselLocationService;
    }

    @Deprecated(forRemoval = true, since = ApiDeprecations.SINCE_FUTURE)
    @Operation(summary = "Find latest vessel locations by mmsi and optional timestamp interval in milliseconds from Unix epoch. " + ApiDeprecations.API_NOTE_FUTURE)
    @GetMapping(path = LATEST_PATH + "/{mmsi}", produces = { MEDIA_TYPE_APPLICATION_JSON,
                                                             MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                             MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public VesselLocationFeatureCollection vesselLocationsByMssiAndTimestamp(
            @Parameter(description = "Maritime Mobile Service Identity (MMSI)", required = true)
            @PathVariable("mmsi")
            final int mmsi,
            @Parameter(description = "From timestamp timestamp in milliseconds from Unix epoch 1970-01-01T00:00:00Z")
            @RequestParam(value = "from", required = false)
            final Long from,
            @Parameter(description = "To timestamp")
            @RequestParam(value = "to", required = false)
            final Long to) {

        LOG.info(String.format("vesselLocationsByMssiAndTimestamp mmsi:\t%d from:\t%d to:\t%d", mmsi, from, to));

        return vesselLocationService.findAllowedLocations(mmsi, from, to);
    }

    @Deprecated(forRemoval = true, since = ApiDeprecations.SINCE_FUTURE)
    @Operation(summary = "Find latest vessel locations by timestamp interval in milliseconds from Unix epoch. " + ApiDeprecations.API_NOTE_FUTURE)
    @GetMapping(path = LATEST_PATH, produces = { MEDIA_TYPE_APPLICATION_JSON,
                                                 MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                 MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public VesselLocationFeatureCollection vesselLocationsByTimestamp(
            @Parameter(description = "From timestamp timestamp in milliseconds from Unix epoch 1970-01-01T00:00:00Z")
            @RequestParam(value = "from", required = false)
            final Long from,
            @Parameter(description = "To timestamp")
            @RequestParam(value = "to", required = false)
            final Long to) {

        LOG.info(String.format("vesselLocationsByTimestamp from:\t%d to:\t%d", from, to));

        return vesselLocationService.findAllowedLocations(null, from, to);
    }

    @Deprecated(forRemoval = true, since = ApiDeprecations.SINCE_FUTURE)
    @Operation(summary = "Find vessel locations within a circle surrounding a point. " + ApiDeprecations.API_NOTE_FUTURE,
               description = "NOTE: Data does not necessarily include all possible vessels. For example fishing boats, vessels without AIS, " +
               "vessels with AIS turned off or vessels outside AIS range will be missing.")
    @GetMapping(path = "latitude/{latitude}/longitude/{longitude}/radius/{radius}/from/{from}", produces = { MEDIA_TYPE_APPLICATION_JSON,
                                                                                                             MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                                                                             MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public VesselLocationFeatureCollection vesselLocationsWithingRadiusFromPoint(
            @Parameter(description = "Radius of search circle in kilometers (km) using haversine formula.", required = true)
            @PathVariable(value = "radius")
            final double radius,
            @Parameter(description = "Latitude of the point", required = true)
            @PathVariable(value = "latitude")
            final double latitude,
            @Parameter(description = "Longitude of the point", required = true)
            @PathVariable(value = "longitude")
            final double longitude,
            @Parameter(description = "Return vessel locations received after given time in ISO date time format {yyyy-MM-dd'T'HH:mm:ss.SSSZ} e.g" +
                ". 2016-10-31T06:30:00.000Z.", required = true)
            @PathVariable(value = "from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            final ZonedDateTime from) {

        return vesselLocationService.findAllowedLocationsWithinRadiusFromPoint(radius, latitude, longitude, from.toInstant().toEpochMilli(), null);
    }

    @Deprecated(forRemoval = true, since = ApiDeprecations.SINCE_FUTURE)
    @Operation(summary = "Find vessel locations within a circle surrounding a vessel. " + ApiDeprecations.API_NOTE_FUTURE,
               description = "NOTE: Data does not necessarily include all possible vessels. For example fishing boats, vessels without AIS, " +
               "vessels with AIS turned off or vessels outside AIS range will be missing.")
    @GetMapping(path = "mmsi/{mmsi}/radius/{radius}/from/{from}", produces = { MEDIA_TYPE_APPLICATION_JSON,
                                                                               MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                                               MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public VesselLocationFeatureCollection vesselLocationsWithingRadiusFromMMSI(
            @Parameter(description = "Radius of search circle in kilometers (km) using haversine formula.", required = true)
            @PathVariable(value = "radius")
            final double radius,
            @Parameter(description = "MMSI of the vessel", required = true)
            @PathVariable(value = "mmsi")
            final int mmsi,
            @Parameter(description = "Return vessel locations received after given time in ISO date time format {yyyy-MM-dd'T'HH:mm:ss.SSSZ} e.g" +
                ". 2016-10-31T06:30:00.000Z.", required = true)
            @PathVariable(value = "from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            final ZonedDateTime from) {

        return vesselLocationService.findAllowedLocationsWithinRadiusFromMMSI(radius, mmsi, from.toInstant().toEpochMilli());
    }
}

