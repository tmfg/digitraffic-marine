package fi.livi.digitraffic.meri.controller.sse;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.config.MarineApplicationConfiguration;
import fi.livi.digitraffic.meri.external.tlsc.sse.TlscSseReports;
import fi.livi.digitraffic.meri.service.sse.SseService;

public class SseControllerTest extends AbstractTestBase {

    @MockBean
    private SseService sseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addSseData() throws Exception {
        final String postJson = readFile("sse/example-sse-report.json");

        final TlscSseReports resultObject = objectMapper.readerFor(TlscSseReports.class).readValue(postJson);

        when(sseService.saveTlscSseReports(ArgumentMatchers.eq(resultObject))).thenReturn(2);

        mockMvc.perform(post(MarineApplicationConfiguration.API_V1_BASE_PATH +
                             MarineApplicationConfiguration.API_SSE_PATH +
                             SseController.ADD_PATH)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(postJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"count\" : 2}"));

    }
}
