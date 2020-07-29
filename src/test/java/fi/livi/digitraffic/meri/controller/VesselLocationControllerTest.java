package fi.livi.digitraffic.meri.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.config.MarineApplicationConfiguration;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeature;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeatureCollection;
import fi.livi.digitraffic.meri.service.ais.VesselLocationService;

public class VesselLocationControllerTest extends AbstractTestBase {

    @MockBean
    private VesselLocationService vesselLocationService;

    private static final int MMSI = 12345;

    @Test
    public void vesselLocationsByMssiEmpty() throws Exception {
        when(vesselLocationService.findAllowedLocations(anyInt(), any(), any())).thenReturn(emptyFeatureCollection());

        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                MarineApplicationConfiguration.API_LOCATIONS_PATH +
                VesselLocationController.LATEST_PATH + "/" + MMSI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.features").isEmpty())
        ;
    }

    @Test
    public void vesselLocationsByMssi() throws Exception {
        when(vesselLocationService.findAllowedLocations(anyInt(), any(), any())).thenReturn(generateFeatureCollection());

        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                MarineApplicationConfiguration.API_LOCATIONS_PATH +
                VesselLocationController.LATEST_PATH + "/" + MMSI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type", is("FeatureCollection")))
                .andExpect(jsonPath("$.features").isNotEmpty())
                .andExpect(jsonPath("$.features[0].mmsi", is(MMSI)))
        ;
    }

    @Test
    public void vesselLocationsByTimestampEmpty() throws Exception {
        when(vesselLocationService.findAllowedLocations(any(), any())).thenReturn(emptyFeatureCollection());

        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                MarineApplicationConfiguration.API_LOCATIONS_PATH +
                VesselLocationController.LATEST_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type", is("FeatureCollection")))
                .andExpect(jsonPath("$.features").isEmpty())
        ;
    }

    @Test
    public void vesselLocationsByTimestamp() throws Exception {
        when(vesselLocationService.findAllowedLocations(any(), any())).thenReturn(generateFeatureCollection());

        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                MarineApplicationConfiguration.API_LOCATIONS_PATH +
                VesselLocationController.LATEST_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.type", is("FeatureCollection")))
                .andExpect(jsonPath("$.features").isNotEmpty())
                .andExpect(jsonPath("$.features[0].mmsi", is(MMSI)))
        ;
    }

    private VesselLocationFeatureCollection generateFeatureCollection() {
        return new VesselLocationFeatureCollection(Collections.singletonList(new VesselLocationFeature(MMSI, null, null)));
    }

    private VesselLocationFeatureCollection emptyFeatureCollection() {
        return new VesselLocationFeatureCollection(Collections.emptyList());
    }
}
