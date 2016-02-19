package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.domain.VesselLocation;
import fi.livi.digitraffic.meri.service.VesselLocationService;

@RestController
public class VesselLocationController {
    private final VesselLocationService vesselLocationService;

    @Autowired
    public VesselLocationController(final VesselLocationService vesselLocationService) {
        this.vesselLocationService = vesselLocationService;
    }

    @RequestMapping(method = RequestMethod.GET, path = API_V1_BASE_PATH + "/locations/history",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<VesselLocation> vesselLocationsByMssi(
            @RequestParam(value = "mmsi", required = false) final Integer mmsi,
            @RequestParam("from") final long from,
            @RequestParam("to") final long to) {
        if(mmsi != null) {
            return vesselLocationService.findLocations(mmsi, from, to);
        } else {
            return vesselLocationService.findLocations(from, to);
        }
    }

}
