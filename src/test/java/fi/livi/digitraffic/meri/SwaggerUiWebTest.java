package fi.livi.digitraffic.meri;

import fi.livi.digitraffic.meri.service.BuildVersionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_LOCATIONS_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_METADATA_PART_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SwaggerUiWebTest extends AbstractTestBase {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private BuildVersionService versionService;

    private final MediaType restContentType = MediaType.APPLICATION_JSON;


    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testSwaggerHome() throws Exception {
        this.mockMvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Swagger UI</title>")));
    }

    @Test
    public void testSwaggerRestApi() throws Exception {
        mockMvc.perform(get("/v2/api-docs?group=metadata-api"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(restContentType))
                .andExpect(jsonPath("$.swagger", is("2.0")))
                .andExpect(jsonPath("$.info.version", is(versionService.getAppFullVersion())))
                .andExpect(jsonPath("$.paths." + API_V1_BASE_PATH + API_METADATA_PART_PATH + API_LOCATIONS_PATH, anything()));
    }

    @Test
    public void testSwaggerRestApiBeta() throws Exception {
        mockMvc.perform(get("/v2/api-docs?group=metadata-api-beta"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(restContentType))
            .andExpect(jsonPath("$.swagger", is("2.0")))
            .andExpect(jsonPath("$.info.version", is(versionService.getAppFullVersion())))
//            .andExpect(content().string(containsString(API_BETA_BASE_PATH + "/")))
        ;
    }
}
