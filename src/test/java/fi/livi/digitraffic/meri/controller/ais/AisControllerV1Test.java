package fi.livi.digitraffic.meri.controller.ais;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.controller.MediaTypes;
import fi.livi.digitraffic.meri.config.MarineApplicationConfiguration;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeature;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeatureCollection;
import fi.livi.digitraffic.meri.service.ais.VesselLocationService;

public class AisControllerV1Test extends AbstractTestBase {

    @MockBean
    private VesselLocationService vesselLocationService;

    private static final int MMSI = 12345;

    @Test
    public void vesselLocationsByMssiEmpty() throws Exception {
        when(vesselLocationService.findAllowedLocations(anyInt(), any(), any())).thenReturn(emptyFeatureCollection());

        mockMvc.perform(get(AisControllerV1.API_AIS_V1 +
                AisControllerV1.LOCATIONS + "?mmsi=" + MMSI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
                .andExpect(jsonPath("$.features").isEmpty())
        ;
    }

    @Test
    public void vesselLocationsByMssi() throws Exception {
        when(vesselLocationService.findAllowedLocations(anyInt(), any(), any())).thenReturn(generateFeatureCollection());

        mockMvc.perform(get(AisControllerV1.API_AIS_V1 +
                AisControllerV1.LOCATIONS + "?mmsi=" + MMSI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
                .andExpect(jsonPath("$.type", is("FeatureCollection")))
                .andExpect(jsonPath("$.features").isNotEmpty())
                .andExpect(jsonPath("$.features[0].mmsi", is(MMSI)))
        ;
    }

    @Test
    public void vesselLocationsByTimestampEmpty() throws Exception {
        when(vesselLocationService.findAllowedLocations(any(), any(), any())).thenReturn(emptyFeatureCollection());

        mockMvc.perform(get(AisControllerV1.API_AIS_V1 +
                AisControllerV1.LOCATIONS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
                .andExpect(jsonPath("$.type", is("FeatureCollection")))
                .andExpect(jsonPath("$.features").isEmpty())
        ;
    }

    @Test
    public void vesselLocationsByTimestamp() throws Exception {
        when(vesselLocationService.findAllowedLocations(any(), any(), any())).thenReturn(generateFeatureCollection());

        mockMvc.perform(get(AisControllerV1.API_AIS_V1 +
                AisControllerV1.LOCATIONS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
                .andExpect(jsonPath("$.type", is("FeatureCollection")))
                .andExpect(jsonPath("$.features").isNotEmpty())
                .andExpect(jsonPath("$.features[0].mmsi", is(MMSI)))
        ;
    }

    private VesselLocationFeatureCollection generateFeatureCollection() {
        return new VesselLocationFeatureCollection(Collections.singletonList(new VesselLocationFeature(MMSI, null, null)), null);
    }

    private VesselLocationFeatureCollection emptyFeatureCollection() {
        return new VesselLocationFeatureCollection(Collections.emptyList(), null);
    }
}
