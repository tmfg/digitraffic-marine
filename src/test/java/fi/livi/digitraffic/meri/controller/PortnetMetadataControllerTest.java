package fi.livi.digitraffic.meri.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.config.MarineApplicationConfiguration;
import fi.livi.digitraffic.meri.controller.portnet.PortnetMetadataController;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;

public class PortnetMetadataControllerTest extends AbstractTestBase {
    @Test
    public void listCodeDescriptions() throws Exception {
        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                MarineApplicationConfiguration.API_METADATA_PART_PATH +
                PortnetMetadataController.CODE_DESCRIPTIONS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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
        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                MarineApplicationConfiguration.API_METADATA_PART_PATH +
                PortnetMetadataController.SSN_LOCATIONS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("ssnLocationFeatureCollection", Matchers.notNullValue()))
                .andExpect(jsonPath("ssnLocationFeatureCollection.features[0]", Matchers.notNullValue()))
                .andExpect(jsonPath("ssnLocationFeatureCollection.features[0].locode", Matchers.notNullValue()))
        ;
    }

    @Test
    public void findSsnLocationByLocode() throws Exception {
        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                MarineApplicationConfiguration.API_METADATA_PART_PATH +
                PortnetMetadataController.SSN_LOCATIONS_PATH +
                "/FIHKO"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("ssnLocationFeatureCollection", Matchers.notNullValue()))
                .andExpect(jsonPath("ssnLocationFeatureCollection.features[0]", Matchers.notNullValue()))
                .andExpect(jsonPath("ssnLocationFeatureCollection.features[0].locode", Matchers.notNullValue()))
        ;
    }

    @Test
    public void findSsnLocationByLocodeInvalidLocation() throws Exception {
        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
            MarineApplicationConfiguration.API_METADATA_PART_PATH +
            PortnetMetadataController.SSN_LOCATIONS_PATH +
            "/FIZZY"))
            .andExpect(status().is(404))
        ;
    }

    @Test
    public void findSsnLocationsByCountryFinland() throws Exception {
        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                MarineApplicationConfiguration.API_METADATA_PART_PATH +
                PortnetMetadataController.SSN_LOCATIONS_BY_COUNTRY_PATH +
                "/Finland"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("ssnLocationFeatureCollection", Matchers.notNullValue()))
                .andExpect(jsonPath("ssnLocationFeatureCollection.features[0]", Matchers.notNullValue()))
                .andExpect(jsonPath("ssnLocationFeatureCollection.features[0].locode", Matchers.notNullValue()))
        ;
    }

    @Test
    public void findSsnLocationsByCountrySweden() throws Exception {
        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
            MarineApplicationConfiguration.API_METADATA_PART_PATH +
            PortnetMetadataController.SSN_LOCATIONS_BY_COUNTRY_PATH +
            "/Sweden"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("ssnLocationFeatureCollection", Matchers.notNullValue()))
            .andExpect(jsonPath("ssnLocationFeatureCollection.features[0]", Matchers.notNullValue()))
            .andExpect(jsonPath("ssnLocationFeatureCollection.features[0].locode", Matchers.notNullValue()))
        ;
    }

    @Test
    public void findSsnLocationsByCountryAlabama() throws Exception {
        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
            MarineApplicationConfiguration.API_METADATA_PART_PATH +
            PortnetMetadataController.SSN_LOCATIONS_BY_COUNTRY_PATH +
            "/Alabama"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("ssnLocationFeatureCollection").exists())
            .andExpect(jsonPath("ssnLocationFeatureCollection.features").exists())
            .andExpect(jsonPath("ssnLocationFeatureCollection.features").isArray())
            .andExpect(jsonPath("ssnLocationFeatureCollection.features").isEmpty())
        ;
    }

    @Test
    public void findVesselDetailsNoParameters() throws Exception {
        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
            MarineApplicationConfiguration.API_METADATA_PART_PATH +
            PortnetMetadataController.VESSEL_DETAILS_PATH))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string("[ ]"))
        ;
    }

}
