package fi.livi.digitraffic.meri.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.config.MarineApplicationConfiguration;


public class PortcallControllerTest extends AbstractTestBase {

    @Test
    public void listAllPortCalls() throws Exception {
        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                MarineApplicationConfiguration.API_PORT_CALLS_PATH +
                "?vesselName=test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
                .andExpect(jsonPath("portCalls", Matchers.notNullValue()))
        ;
    }

    @Test
    public void listPortCallsFromLocodeByVesselName() throws Exception {
        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                MarineApplicationConfiguration.API_PORT_CALLS_PATH +
                "/FIHEL?vesselName=test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
                .andExpect(jsonPath("portCalls", Matchers.notNullValue()))
        ;
    }

    @Test
    public void listAllPortCallsFromLocodeSucceeds() throws Exception {
        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                MarineApplicationConfiguration.API_PORT_CALLS_PATH + "/FIHEL"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
                .andExpect(jsonPath("portCalls", Matchers.notNullValue()))
        ;
    }
}
