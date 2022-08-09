package fi.livi.digitraffic.meri.controller.sse;

import static java.time.ZoneOffset.UTC;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.config.MarineApplicationConfiguration;
import fi.livi.digitraffic.meri.service.sse.SseFeatureCollectionBuilder;
import fi.livi.digitraffic.meri.service.sse.SseService_V1;

public class SseController_V1Test extends AbstractTestBase {

    @MockBean
    private SseService_V1 sseServiceV1;

    @Test
    public void sseLatest() throws Exception {
        final String lastUpdate = "2019-01-11T10:00:01+03:00";
        when(sseServiceV1.findLatest())
            .thenReturn(new SseFeatureCollectionBuilder(ZonedDateTime.parse(lastUpdate).toInstant()).build());

        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                            MarineApplicationConfiguration.API_SSE_PATH + SseController_V1.LATEST_PATH))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.features[0].siteNumber", Matchers.is(0)))
            .andExpect(jsonPath("$.features[0].properties.lastUpdate",
                                Matchers.is(ZonedDateTime.parse(lastUpdate).toInstant().atZone(UTC).toString())))
        ;
    }

    @Test
    public void sseHistory() throws Exception {
        final String start = "2019-01-10T10:00:01+03:00";
        final String end = "2019-01-11T10:00:01+03:00";
        when(sseServiceV1.findHistory(ZonedDateTime.parse(start).toInstant(), ZonedDateTime.parse(end).toInstant()))
            .thenReturn(new SseFeatureCollectionBuilder(ZonedDateTime.parse(end).toInstant()).build());

        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                            MarineApplicationConfiguration.API_SSE_PATH + SseController_V1.HISTORY_PATH)
                .param("from", start)
                .param("to", end))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.features[0].siteNumber", Matchers.is(0)))
            .andExpect(jsonPath("$.features[0].properties.lastUpdate", Matchers.is(ZonedDateTime.parse(end).toInstant().atZone(UTC).toString())))
        ;
    }

    @Test
    public void sseHistoryWithSiteNumber() throws Exception {
        final int siteNumber = 1234;
        final String start = "2019-01-10T10:00:01+03:00";
        final String end = "2019-01-11T10:00:01+03:00";
        when(sseServiceV1.findHistory(siteNumber, ZonedDateTime.parse(start).toInstant(), ZonedDateTime.parse(end).toInstant()))
            .thenReturn(new SseFeatureCollectionBuilder(ZonedDateTime.parse(end).toInstant()).build());

        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
            MarineApplicationConfiguration.API_SSE_PATH + SseController_V1.HISTORY_PATH + "/" + siteNumber)
            .param("from", start)
            .param("to", end))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.features[0].siteNumber", Matchers.is(0)))
            .andExpect(jsonPath("$.features[0].properties.lastUpdate", Matchers.is(ZonedDateTime.parse(end).toInstant().atZone(UTC).toString())))
        ;
    }
}
