package fi.livi.digitraffic.meri;

import static fi.livi.digitraffic.meri.controller.portcall.PortcallControllerV1.API_PORT_CALL_V1;
import static fi.livi.digitraffic.meri.controller.portcall.PortcallControllerV1.PORT_CALLS;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import fi.livi.digitraffic.meri.service.BuildVersionService;

public class SwaggerUiWebTest extends AbstractWebTestBase {

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
    public void testSwaggerRestMarineApi() throws Exception {
        mockMvc.perform(get("/v3/api-docs/marine-api"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(restContentType))
            .andExpect(jsonPath("$.openapi", is("3.1.0")))
            .andExpect(jsonPath("$.info.version", is(versionService.getAppFullVersion())))
            .andExpect(jsonPath("$.paths." + API_PORT_CALL_V1 + PORT_CALLS, anything()));
    }

    @Test
    public void testMarineSwaggerRestMarineApiBeta() throws Exception {
        mockMvc.perform(get("/v3/api-docs/marine-api-beta"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(restContentType))
            .andExpect(jsonPath("$.openapi", is("3.1.0")))
            .andExpect(jsonPath("$.info.version", is(versionService.getAppFullVersion())))
        //            .andExpect(content().string(containsString(API_BETA_BASE_PATH + "/")))
        ;
    }
}
