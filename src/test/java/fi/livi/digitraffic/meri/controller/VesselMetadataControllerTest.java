package fi.livi.digitraffic.meri.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import fi.livi.digitraffic.meri.AisTestApplicationConfig;
import fi.livi.digitraffic.meri.config.AisApplicationConfiguration;
import fi.livi.digitraffic.meri.domain.VesselMetadata;
import fi.livi.digitraffic.meri.service.VesselMetadataService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AisTestApplicationConfig.class)
@AutoConfigureMockMvc
public class VesselMetadataControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VesselMetadataService vesselMetadataService;

    private static final int MMSI = 12345;

    @Test
    public void testAllVesselsMetadata() throws Exception {
        given(vesselMetadataService.listAllVesselMetadata()).willReturn(Arrays.asList(generateVesselMetadata()));

        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_METADATA_PART_PATH +
                VesselMetadataController.VESSELS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].mmsi", is(MMSI)))
                ;
    }

    private VesselMetadata generateVesselMetadata() {
        final VesselMetadata metadata = new VesselMetadata();

        metadata.setMmsi(MMSI);

        return metadata;
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
        given(vesselMetadataService.findMetadataByMssi(anyInt())).willReturn(generateVesselMetadata());

        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_METADATA_PART_PATH +
                VesselMetadataController.VESSELS_PATH +
                "/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mmsi", is(MMSI)))
        ;
    }
}
