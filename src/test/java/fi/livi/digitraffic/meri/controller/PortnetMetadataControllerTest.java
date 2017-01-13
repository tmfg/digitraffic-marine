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
import fi.livi.digitraffic.meri.controller.portnet.PortnetMetadataController;

public class PortnetMetadataControllerTest extends AbstractControllerTest {
    @Test
    public void listCodeDescriptions() throws Exception {
        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_METADATA_PART_PATH +
                PortnetMetadataController.CODE_DESCRIPTIONS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("agentTypes", Matchers.notNullValue()))
                .andExpect(jsonPath("agentTypes[0].code", Matchers.notNullValue()))
                .andExpect(jsonPath("agentTypes[0].description", Matchers.notNullValue()))
                .andExpect(jsonPath("cargoTypes", Matchers.notNullValue()))
                .andExpect(jsonPath("cargoTypes[0].code", Matchers.notNullValue()))
                .andExpect(jsonPath("cargoTypes[0].description", Matchers.notNullValue()))
                .andExpect(jsonPath("vesselTypes", Matchers.notNullValue()))
                .andExpect(jsonPath("vesselTypes[0].code", Matchers.notNullValue()))
                .andExpect(jsonPath("vesselTypes[0].description", Matchers.notNullValue()))
        ;
    }

    @Test
    public void listAllMetadata() throws Exception {
        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_METADATA_PART_PATH +
                PortnetMetadataController.SSN_LOCATIONS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("ssnLocationFeatureCollection", Matchers.notNullValue()))
                .andExpect(jsonPath("ssnLocationFeatureCollection.features[0]", Matchers.notNullValue()))
                .andExpect(jsonPath("ssnLocationFeatureCollection.features[0].locode", Matchers.notNullValue()))
        ;
    }

    @Test
    public void findSsnLocationByLocode() throws Exception {
        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_METADATA_PART_PATH +
                PortnetMetadataController.SSN_LOCATIONS_PATH +
                "/FIHKO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("ssnLocationFeatureCollection", Matchers.notNullValue()))
                .andExpect(jsonPath("ssnLocationFeatureCollection.features[0]", Matchers.notNullValue()))
                .andExpect(jsonPath("ssnLocationFeatureCollection.features[0].locode", Matchers.notNullValue()))
        ;
    }

    @Test
    public void findSsnLocationsByCountry() throws Exception {
        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_METADATA_PART_PATH +
                PortnetMetadataController.SSN_LOCATIONS_BY_COUNTRY_PATH +
                "/Finland"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("ssnLocationFeatureCollection", Matchers.notNullValue()))
                .andExpect(jsonPath("ssnLocationFeatureCollection.features[0]", Matchers.notNullValue()))
                .andExpect(jsonPath("ssnLocationFeatureCollection.features[0].locode", Matchers.notNullValue()))
        ;
    }

}
