package fi.livi.digitraffic.meri.controller.sse;

import static java.time.ZoneOffset.UTC;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.config.MarineApplicationConfiguration;
import fi.livi.digitraffic.meri.dao.sse.SseReportRepository;
import fi.livi.digitraffic.meri.external.tlsc.sse.TlscSseReports;
import fi.livi.digitraffic.meri.service.sse.SseFeatureCollectionBuilder;
import fi.livi.digitraffic.meri.service.sse.SseService;

public class SseControllerTest extends AbstractTestBase {

    @MockBean
    private SseService sseService;

    @MockBean
    private SseReportRepository sseReportRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addSseData() throws Exception {
        final String postJson = readFile("sse/example-sse-report1.json");

        final TlscSseReports resultObject = objectMapper.readerFor(TlscSseReports.class).readValue(postJson);

        when(sseService.saveTlscSseReports(ArgumentMatchers.eq(resultObject))).thenReturn(2);

        System.out.println(MarineApplicationConfiguration.API_V1_BASE_PATH +
            MarineApplicationConfiguration.API_SSE_PATH +
            SseController.ADD_PATH);
        mockMvc.perform(post(MarineApplicationConfiguration.API_V1_BASE_PATH +
                             MarineApplicationConfiguration.API_SSE_PATH +
                             SseController.ADD_PATH)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(postJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"count\" : 2}"));

    }

    @Test
    public void sseLatest() throws Exception {
        final String lastUpdate = "2019-01-11T10:00:01+03:00";
        when(sseService.findLatest())
            .thenReturn(new SseFeatureCollectionBuilder(ZonedDateTime.parse(lastUpdate)).build());

        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                            MarineApplicationConfiguration.API_SSE_PATH + SseController.LATEST_PATH))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.features[0].siteNumber", Matchers.is(0)))
            .andExpect(jsonPath("$.features[0].properties.lastUpdate",
                                Matchers.is(ZonedDateTime.parse(lastUpdate).toInstant().atZone(UTC).toString())))
        ;
    }

    @Test
    public void sseHistory() throws Exception {
        final String start = "2019-01-10T10:00:01+03:00";
        final String end = "2019-01-11T10:00:01+03:00";
        when(sseService.findHistory(ZonedDateTime.parse(start), ZonedDateTime.parse(end)))
            .thenReturn(new SseFeatureCollectionBuilder(ZonedDateTime.parse(end)).build());

        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                            MarineApplicationConfiguration.API_SSE_PATH + SseController.HISTORY_PATH)
                .param("from", start)
                .param("to", end))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.features[0].siteNumber", Matchers.is(0)))
            .andExpect(jsonPath("$.features[0].properties.lastUpdate", Matchers.is(ZonedDateTime.parse(end).toInstant().atZone(UTC).toString())))
        ;
    }

    @Test
    public void sseHistoryWithSiteNumber() throws Exception {
        final int siteNumber = 1234;
        final String start = "2019-01-10T10:00:01+03:00";
        final String end = "2019-01-11T10:00:01+03:00";
        when(sseService.findHistory(siteNumber, ZonedDateTime.parse(start), ZonedDateTime.parse(end)))
            .thenReturn(new SseFeatureCollectionBuilder(ZonedDateTime.parse(end)).build());

        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
            MarineApplicationConfiguration.API_SSE_PATH + SseController.HISTORY_PATH + "/" + siteNumber)
            .param("from", start)
            .param("to", end))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.features[0].siteNumber", Matchers.is(0)))
            .andExpect(jsonPath("$.features[0].properties.lastUpdate", Matchers.is(ZonedDateTime.parse(end).toInstant().atZone(UTC).toString())))
        ;
    }
}
