package fi.livi.digitraffic.meri.controller.v2;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.http.MediaType;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.config.MarineApplicationConfiguration;
import fi.livi.digitraffic.meri.controller.portnet.PortnetMetadataController;

public class V2PortnetMetadataControllerTest extends AbstractTestBase {
    @Test
    public void listCodeDescriptions() throws Exception {
        mockMvc.perform(get(MarineApplicationConfiguration.API_V2_BASE_PATH +
                MarineApplicationConfiguration.API_METADATA_PART_PATH +
                PortnetMetadataController.CODE_DESCRIPTIONS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("agentTypes", Matchers.notNullValue()))
                .andExpect(jsonPath("agentTypes[0].code", Matchers.notNullValue()))
                .andExpect(jsonPath("agentTypes[0].descriptionFi", Matchers.notNullValue()))
                .andExpect(jsonPath("agentTypes[0].descriptionEn", Matchers.notNullValue()))
                .andExpect(jsonPath("cargoTypes", Matchers.notNullValue()))
                .andExpect(jsonPath("cargoTypes[0].code", Matchers.notNullValue()))
                .andExpect(jsonPath("cargoTypes[0].descriptionFi", Matchers.notNullValue()))
                .andExpect(jsonPath("cargoTypes[0].descriptionEn", Matchers.notNullValue()))
                .andExpect(jsonPath("vesselTypes", Matchers.notNullValue()))
                .andExpect(jsonPath("vesselTypes[0].code", Matchers.notNullValue()))
                .andExpect(jsonPath("vesselTypes[0].descriptionFi", Matchers.notNullValue()))
                .andExpect(jsonPath("vesselTypes[0].descriptionEn", Matchers.notNullValue()))
        ;
    }
}
