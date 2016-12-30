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

import fi.livi.digitraffic.meri.config.AisApplicationConfiguration;
import fi.livi.digitraffic.meri.model.ais.VesselMetadataJson;
import fi.livi.digitraffic.meri.service.ais.VesselMetadataService;

public class VesselMetadataControllerTest extends AbstractControllerTest {
    @MockBean
    private VesselMetadataService vesselMetadataService;

    private static final int MMSI = 12345;

    @Test
    public void testAllVesselsMetadata() throws Exception {
        given(vesselMetadataService.listAllowedVesselMetadata()).willReturn(Collections.singletonList(generateVesselMetadata()));

        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_METADATA_PART_PATH +
                VesselMetadataController.VESSELS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].mmsi", is(MMSI)))
                ;
    }

    private VesselMetadataJson generateVesselMetadata() {
        return new VesselMetadataJson() {
            @Override public int getMmsi() {
                return MMSI;
            }

            @Override public String getName() {
                return null;
            }

            @Override public int getShipType() {
                return 0;
            }

            @Override public long getReferencePointA() {
                return 0;
            }

            @Override public long getReferencePointB() {
                return 0;
            }

            @Override public long getReferencePointC() {
                return 0;
            }

            @Override public long getReferencePointD() {
                return 0;
            }

            @Override public int getPosType() {
                return 0;
            }

            @Override public int getDraught() {
                return 0;
            }

            @Override public int getImo() {
                return 0;
            }

            @Override public String getCallSign() {
                return null;
            }

            @Override public long getEta() {
                return 0;
            }

            @Override public long getTimestamp() {
                return 0;
            }

            @Override public String getDestination() {
                return null;
            }
        };
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
        given(vesselMetadataService.findAllowedMetadataByMssi(anyInt())).willReturn(generateVesselMetadata());

        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_METADATA_PART_PATH +
                VesselMetadataController.VESSELS_PATH +
                "/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mmsi", is(MMSI)))
        ;
    }
}
