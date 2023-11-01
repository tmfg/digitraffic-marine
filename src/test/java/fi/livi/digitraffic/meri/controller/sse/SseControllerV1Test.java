package fi.livi.digitraffic.meri.controller.sse;

import static fi.livi.digitraffic.meri.controller.sse.SseControllerV1.API_SSE_V1;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.mock.mockito.MockBean;

import fi.livi.digitraffic.meri.AbstractWebTestBase;
import fi.livi.digitraffic.meri.controller.MediaTypes;
import fi.livi.digitraffic.meri.service.sse.v1.SseFeatureCollectionBuilder;
import fi.livi.digitraffic.meri.service.sse.v1.SseWebServiceV1;

public class SseControllerV1Test extends AbstractWebTestBase {

    @MockBean
    private SseWebServiceV1 sseWebServiceV1;

    private static final String SSE_MEASUREMENTS_PATH = API_SSE_V1 + "/measurements";

    @Test
    public void sseLatest() throws Exception {
        final String lastUpdate = "2019-01-11T10:00:01.000Z";
        when(sseWebServiceV1.findMeasurements(null))
            .thenReturn(new SseFeatureCollectionBuilder(Instant.parse(lastUpdate)).build());

        mockMvc.perform(get(SSE_MEASUREMENTS_PATH))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
            .andExpect(jsonPath("$.features[0].siteNumber", Matchers.is(0)))
            .andExpect(jsonPath("$.features[0].properties.lastUpdate",
                Matchers.is(Instant.parse(lastUpdate).toString())))
        ;
    }

    @Test
    public void sseHistory() throws Exception {
        final String start = "2019-01-10T10:00:01.000Z";
        final String end = "2019-01-11T10:00:01.000Z";
        when(sseWebServiceV1.findHistory(ArgumentMatchers.isNull(), ArgumentMatchers.any(Instant.class), ArgumentMatchers.any(Instant.class)))
            .thenReturn(new SseFeatureCollectionBuilder(Instant.parse(end)).build());

        mockMvc.perform(get(SSE_MEASUREMENTS_PATH)
                .param("from", start)
                .param("to", end))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
            .andExpect(jsonPath("$.features[0].siteNumber", Matchers.is(0)))
            .andExpect(jsonPath("$.features[0].properties.lastUpdate", Matchers.is(Instant.parse(end).toString())))
        ;
    }

    @Test
    public void sseHistoryWithSiteNumber() throws Exception {
        final int siteNumber = 1234;
        final String start = "2019-01-10T10:00:01.000Z";
        final String end = "2019-01-11T10:00:01.000Z";
        when(sseWebServiceV1.findHistory(ArgumentMatchers.eq(siteNumber), ArgumentMatchers.any(Instant.class), ArgumentMatchers.any(Instant.class)))
            .thenReturn(new SseFeatureCollectionBuilder(Instant.parse(end)).build());

        mockMvc.perform(get(SSE_MEASUREMENTS_PATH)
                .param("siteNumber", String.valueOf(siteNumber))
                .param("from", start)
                .param("to", end))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.MEDIA_TYPE_APPLICATION_JSON))
            .andExpect(jsonPath("$.features[0].siteNumber", Matchers.is(0)))
            .andExpect(jsonPath("$.features[0].properties.lastUpdate", Matchers.is(Instant.parse(end).toString())))
        ;
    }
}

