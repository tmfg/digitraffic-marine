package fi.livi.digitraffic.meri.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import fi.livi.digitraffic.meri.AisTestApplicationConfig;
import fi.livi.digitraffic.meri.config.AisApplicationConfiguration;
import fi.livi.digitraffic.meri.controller.portnet.PortnetMetadataController;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AisTestApplicationConfig.class)
@AutoConfigureMockMvc
public class PortnetMetadataControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllSsnMetadata() throws Exception {
        mockMvc.perform(get(AisApplicationConfiguration.API_V1_BASE_PATH +
                AisApplicationConfiguration.API_METADATA_PART_PATH +
                PortnetMetadataController.SSN_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("locations[0].locode", Matchers.notNullValue()))
        ;
    }

}
