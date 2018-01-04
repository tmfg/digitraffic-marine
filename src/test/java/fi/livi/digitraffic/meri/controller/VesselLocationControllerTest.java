package fi.livi.digitraffic.meri.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.config.AisApplicationConfiguration;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeature;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeatureCollection;
import fi.livi.digitraffic.meri.service.ais.VesselLocationService;

public class VesselLocationControllerTest extends AbstractTestBase {

    @MockBean
    private VesselLocationService vesselLocationService;

    private static final int MMSI = 12345;

    @Test
    public void vesselLocationsByMssiEmpty() throws Exception {
        when(vesselLocationService.findAllowedLocations(anyInt(), Matchers.any(), Matchers.any())).thenReturn(new VesselLocationFeatureCollection(Collections.emptyList()));

        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_LOCATIONS_PATH +
                VesselLocationController.LATEST_PATH + "/" + MMSI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.features").isEmpty())
        ;
    }

    @Test
    public void vesselLocationsByMssi() throws Exception {
        final VesselLocationFeature message = generateVesselLocation();
        when(vesselLocationService.findAllowedLocations(anyInt(), Matchers.any(), Matchers.any())).thenReturn(new VesselLocationFeatureCollection(Collections.singletonList(message)));

        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_LOCATIONS_PATH +
                VesselLocationController.LATEST_PATH + "/" + MMSI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.type", is("FeatureCollection")))
                .andExpect(jsonPath("$.features").isNotEmpty())
                .andExpect(jsonPath("$.features[0].mmsi", is(MMSI)))
        ;
    }

    @Test
    public void vesselLocationsByTimestampEmpty() throws Exception {
        when(vesselLocationService.findAllowedLocations(anyLong(), anyLong())).thenReturn(new VesselLocationFeatureCollection(Collections.emptyList()));

        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_LOCATIONS_PATH +
                VesselLocationController.LATEST_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.type", is("FeatureCollection")))
                .andExpect(jsonPath("$.features").isEmpty())
        ;
    }

    @Test
    public void vesselLocationsByTimestamp() throws Exception {
        final VesselLocationFeature message = generateVesselLocation();
        when(vesselLocationService.findAllowedLocations(anyLong(), anyLong())).thenReturn(new VesselLocationFeatureCollection(Collections.singletonList(message)));

        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_LOCATIONS_PATH +
                VesselLocationController.LATEST_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.type", is("FeatureCollection")))
                .andExpect(jsonPath("$.features").isNotEmpty())
                .andExpect(jsonPath("$.features[0].mmsi", is(MMSI)))
        ;
    }

    private VesselLocationFeature generateVesselLocation() {
        return new VesselLocationFeature(MMSI, null, null);
    }
}
