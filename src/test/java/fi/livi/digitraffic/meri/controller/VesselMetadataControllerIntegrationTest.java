package fi.livi.digitraffic.meri.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.VesselMetadataBuilder;
import fi.livi.digitraffic.meri.config.MarineApplicationConfiguration;
import fi.livi.digitraffic.meri.dao.ais.VesselMetadataRepository;

public class VesselMetadataControllerIntegrationTest extends AbstractTestBase {
    @MockBean
    private VesselMetadataRepository vesselMetadataRepository;

    private static final int MMSI = 12345;

    @Test
    public void vesselMetadataByMssiNotFound() throws Exception {
        when(vesselMetadataRepository.findByMmsi(anyInt())).thenReturn(null);

        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                MarineApplicationConfiguration.API_METADATA_PART_PATH +
                VesselMetadataController.VESSELS_PATH + "/" + MMSI))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void vesselMetadataByMssi() throws Exception {
        when(vesselMetadataRepository.findByMmsi(anyInt())).thenReturn(new VesselMetadataBuilder(MMSI).build());

        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                MarineApplicationConfiguration.API_METADATA_PART_PATH +
        VesselMetadataController.VESSELS_PATH + "/" + MMSI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mmsi", Matchers.is(MMSI)))
        ;
    }

    @Test
    public void vesselMetadataByMssiForbiddenShiptype() throws Exception {
        when(vesselMetadataRepository.findByMmsi(anyInt())).thenReturn(new VesselMetadataBuilder(MMSI).shipType(30).build());

                mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                        MarineApplicationConfiguration.API_METADATA_PART_PATH +
                        VesselMetadataController.VESSELS_PATH + "/" + MMSI))
                        .andExpect(status().isNotFound())
        ;
    }

}
