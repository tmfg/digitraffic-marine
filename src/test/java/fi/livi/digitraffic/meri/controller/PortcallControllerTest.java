package fi.livi.digitraffic.meri.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.http.MediaType;

import fi.livi.digitraffic.meri.AbstractControllerTest;
import fi.livi.digitraffic.meri.config.AisApplicationConfiguration;


public class PortcallControllerTest extends AbstractControllerTest {
    @Test
    public void listAllPortCallsFail() throws Exception {
        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_PORT_CALLS_PATH))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    public void listAllPortCalls() throws Exception {
        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_PORT_CALLS_PATH +
                "?vesselName=test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("portCalls", Matchers.notNullValue()))
        ;
    }

    @Test
    public void listAllPortCallsFromLocode() throws Exception {
        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_PORT_CALLS_PATH +
                "/FIHEL?vesselName=test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("portCalls", Matchers.notNullValue()))
        ;
    }

    @Test
    public void listAllPortCallsFromLocodeFail() throws Exception {
        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_PORT_CALLS_PATH + "/FIHEL"))
                .andExpect(status().isBadRequest())
        ;
    }
}
