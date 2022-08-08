package fi.livi.digitraffic.meri.controller.sse;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.service.sse.SseFeatureCollectionBuilder;
import fi.livi.digitraffic.meri.service.sse.SseServiceV1;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.time.ZonedDateTime;

import static fi.livi.digitraffic.meri.controller.sse.SseControllerV1.API_SSE_BETA;
import static java.time.ZoneOffset.UTC;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SseControllerV1Test extends AbstractTestBase {

    @MockBean
    private SseServiceV1 sseServiceV1;

    private static final String SSE_MEASUREMENTS_PATH = API_SSE_BETA + "/measurements";

    @Test
    public void sseLatest() throws Exception {
        final String lastUpdate = "2019-01-11T10:00:01+03:00";
        when(sseServiceV1.findMeasurements(null))
            .thenReturn(new SseFeatureCollectionBuilder(ZonedDateTime.parse(lastUpdate).toInstant()).build());

        mockMvc.perform(get(SSE_MEASUREMENTS_PATH))
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
        when(sseServiceV1.findHistory(null, ArgumentMatchers.any(Instant.class), ArgumentMatchers.any(Instant.class)))
            .thenReturn(new SseFeatureCollectionBuilder(ZonedDateTime.parse(end).toInstant()).build());

        mockMvc.perform(get(SSE_MEASUREMENTS_PATH)
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
        when(sseServiceV1.findHistory(siteNumber, ArgumentMatchers.any(Instant.class), ArgumentMatchers.any(Instant.class)))
            .thenReturn(new SseFeatureCollectionBuilder(ZonedDateTime.parse(end).toInstant()).build());

        mockMvc.perform(get(SSE_MEASUREMENTS_PATH)
                .param("siteNumber", String.valueOf(siteNumber))
                .param("from", start)
                .param("to", end))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.features[0].siteNumber", Matchers.is(0)))
            .andExpect(jsonPath("$.features[0].properties.lastUpdate", Matchers.is(ZonedDateTime.parse(end).toInstant().atZone(UTC).toString())))
        ;
    }
}

