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
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import fi.livi.digitraffic.meri.AisTestApplicationConfig;
import fi.livi.digitraffic.meri.config.AisApplicationConfiguration;
import fi.livi.digitraffic.meri.model.ais.VesselLocationJson;
import fi.livi.digitraffic.meri.service.ais.VesselLocationService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AisTestApplicationConfig.class)
@AutoConfigureMockMvc
public class VesselLocationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VesselLocationService vesselLocationService;

    private static final int MMSI = 12345;

    @Test
    public void testVesselLocationsByMssi() throws Exception {
        final VesselLocationJson message = generateVesselLocation();
        when(vesselLocationService.findLocations(anyInt(), Matchers.any(), Matchers.any())).thenReturn(Collections.singletonList(message));

        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_LOCATIONS_PATH +
                VesselLocationController.LATEST_PATH + "/" + MMSI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].mmsi", is(MMSI)))
        ;
    }

    @Test
    public void testVesselLocationsByTimestamp() throws Exception {
        final VesselLocationJson message = generateVesselLocation();
        when(vesselLocationService.findLocations(anyLong(), anyLong())).thenReturn(Collections.singletonList(message));

        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_LOCATIONS_PATH +
                VesselLocationController.LATEST_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].mmsi", is(MMSI)))
        ;
    }

    private VesselLocationJson generateVesselLocation() {
        return new VesselLocationJson() {
            @Override public int getMmsi() {
                return MMSI;
            }

            @Override public double getX() {
                return 0;
            }

            @Override public double getY() {
                return 0;
            }

            @Override public double getSog() {
                return 0;
            }

            @Override public double getCog() {
                return 0;
            }

            @Override public int getNavStat() {
                return 0;
            }

            @Override public int getRot() {
                return 0;
            }

            @Override public boolean isPosAcc() {
                return false;
            }

            @Override public boolean isRaim() {
                return false;
            }

            @Override public Integer getHeading() {
                return null;
            }

            @Override public long getTimestamp() {
                return 0;
            }

            @Override public long getTimestampExternal() {
                return 0;
            }
        };
    }

}
