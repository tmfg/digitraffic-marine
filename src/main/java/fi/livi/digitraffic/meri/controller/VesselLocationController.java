package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;

import java.time.ZonedDateTime;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.domain.VesselLocation;
import fi.livi.digitraffic.meri.service.VesselLocationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class VesselLocationController {

    private static final Logger LOG = Logger.getLogger(VesselLocationController.class);

    private final VesselLocationService vesselLocationService;

    @Autowired
    public VesselLocationController(final VesselLocationService vesselLocationService) {
        this.vesselLocationService = vesselLocationService;
    }

    @ApiOperation("Find vessel locations by mmsi and optional timestamp interval in milliseconds from Unix epoch.")
    @RequestMapping(method = RequestMethod.GET, path = API_V1_BASE_PATH + "/locations/history/{mmsi}",
                    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    @ApiOperation("Find vessel locations by mmsi and optional timestamp interval in ISO 8601 datetime format.")
    @RequestMapping(method = RequestMethod.GET, path = API_V1_BASE_PATH + "/locations/history/datetime/{mmsi}",
                    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<VesselLocation> vesselLocationsByMssiAndDateTime(
            @ApiParam(value = "Maritime Mobile Service Identity (MMSI)", required = true)
            @PathVariable("mmsi")
            final int mmsi,
            @ApiParam("From timestamp in ISO 8601 datetime format. (ie. 2015-05-19T03:28:28.781+03:00)")
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            final ZonedDateTime from,
            @ApiParam("To timestamp in ISO 8601 datetime format. (ie. 2015-05-19T00:28:47.434Z")
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            final ZonedDateTime to) {

        Long fromMs = null;
        Long toMs = null;
        if (from != null) {
            fromMs = from.toInstant().toEpochMilli();
        }
        if (to != null) {
            toMs = to.toInstant().toEpochMilli();
        }
        LOG.info("vesselLocationsByMssiAndDateTime mmsi:\t" + mmsi + "from:\t" + from + " fromMs\t" + fromMs + " to:\t" + to + " toMs\t" + toMs);
        return IteratorUtils.toList( vesselLocationService.findLocations(mmsi, fromMs, toMs).iterator() );
    }

    @ApiOperation("Find vessel locations by timestamp interval in milliseconds from Unix epoch.")
    @RequestMapping(method = RequestMethod.GET, path = API_V1_BASE_PATH + "/locations/history",
                    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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


    @ApiOperation("Find vessel locations by timestamp interval in ISO 8601 datetime format.")
    @RequestMapping(method = RequestMethod.GET, path = API_V1_BASE_PATH + "/locations/history/datetime",
                    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<VesselLocation> vesselLocationsByDateTime(
            @ApiParam("From timestamp in ISO 8601 datetime format. (ie. 2015-05-19T03:28:28.781+03:00)")
            @RequestParam(value = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            final ZonedDateTime from,
            @ApiParam("To timestamp in ISO 8601 datetime format. (ie. 2015-05-19T00:28:47.434Z")
            @RequestParam(value = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            final ZonedDateTime to) {

        Long fromMs = null;
        Long toMs = null;
        if (from != null) {
            fromMs = from.toInstant().toEpochMilli();
        }
        if (to != null) {
            toMs = to.toInstant().toEpochMilli();
        }
        LOG.info("vesselLocationsByDateTime from:\t" + from + " fromMs\t" + fromMs + " to:\t" + to + " toMs\t" + toMs);
        return IteratorUtils.toList( vesselLocationService.findLocations(fromMs, toMs).iterator() );
    }
}
