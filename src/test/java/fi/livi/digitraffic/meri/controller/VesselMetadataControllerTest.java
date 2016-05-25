package fi.livi.digitraffic.meri.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import fi.livi.digitraffic.meri.RestTest;
import fi.livi.digitraffic.meri.config.AisApplicationConfiguration;

public class VesselMetadataControllerTest extends RestTest {
    @Test
    public void testAllVesselsMetadata() throws Exception {
        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_METADATA_PART_PATH +
                VesselMetadataController.VESSELS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        // TODO: add test data and check for it
                ;
    }

    @Test
    public void testVesselMetadataMetadataNotFound() throws Exception {
        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_METADATA_PART_PATH +
                VesselMetadataController.VESSELS_PATH +
                "/12345"))
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void testVesselMetadataMetadataFound() throws Exception {
        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_METADATA_PART_PATH +
                VesselMetadataController.VESSELS_PATH +
                "/42"))
                .andExpect(status().isOk())
        //                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        // TODO: add test data and check for it
        ;
    }
}
