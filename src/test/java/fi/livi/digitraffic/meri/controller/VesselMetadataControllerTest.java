package fi.livi.digitraffic.meri.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.VesselMetadataBuilder;
import fi.livi.digitraffic.meri.config.AisApplicationConfiguration;
import fi.livi.digitraffic.meri.service.ais.VesselMetadataService;

public class VesselMetadataControllerTest extends AbstractTestBase {
    @MockBean
    private VesselMetadataService vesselMetadataService;

    private static final int MMSI = 12345;

    @Test
    public void allVesselsEmpty() throws Exception {
        given(vesselMetadataService.findAllowedVesselMetadataFrom(null)).willReturn(Collections.emptyList());

        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_METADATA_PART_PATH +
                VesselMetadataController.VESSELS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("[]"))
        ;
    }

    @Test
    public void allVessels() throws Exception {
        given(vesselMetadataService.findAllowedVesselMetadataFrom(null)).willReturn(Collections.singletonList(new VesselMetadataBuilder(MMSI).build()));

        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_METADATA_PART_PATH +
                VesselMetadataController.VESSELS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].mmsi", is(MMSI)))
                ;
    }

    @Test
    public void allVesselsFrom() throws Exception {
        given(vesselMetadataService.findAllowedVesselMetadataFrom(1L)).willReturn(Collections.singletonList(new VesselMetadataBuilder(MMSI).build()));

        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                            AisApplicationConfiguration.API_METADATA_PART_PATH +
                            VesselMetadataController.VESSELS_PATH + "?from=1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$[0].mmsi", is(MMSI)))
        ;
    }

    @Test
    public void testVesselMetadataMetadataNotFound() throws Exception {
        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_METADATA_PART_PATH +
                VesselMetadataController.VESSELS_PATH +
                "/" + MMSI))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
        ;
    }

    @Test
    public void testVesselMetadataMetadataFound() throws Exception {
        given(vesselMetadataService.findAllowedMetadataByMssi(anyInt())).willReturn(new VesselMetadataBuilder(MMSI).build());

        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_METADATA_PART_PATH +
                VesselMetadataController.VESSELS_PATH +
                "/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mmsi", is(MMSI)))
        ;
    }
}
