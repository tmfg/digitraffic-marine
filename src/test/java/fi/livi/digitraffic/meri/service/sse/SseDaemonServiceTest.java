package fi.livi.digitraffic.meri.service.sse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import fi.livi.digitraffic.common.util.ThreadUtil;
import fi.livi.digitraffic.common.util.TimeUtil;
import fi.livi.digitraffic.meri.AbstractDaemonTestBase;
import fi.livi.digitraffic.meri.dao.sse.SseReportRepository;
import fi.livi.digitraffic.meri.dto.sse.v1.SseFeatureCollectionV1;
import fi.livi.digitraffic.meri.dto.sse.v1.SseFeatureV1;
import fi.livi.digitraffic.meri.dto.sse.v1.SsePropertiesV1;
import fi.livi.digitraffic.meri.model.sse.SseReport;
import jakarta.transaction.Transactional;

@Transactional
public class SseDaemonServiceTest extends AbstractDaemonTestBase {

    @SpyBean
    private SseDaemonService sseServiceV1;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SseReportRepository sseReportRepository;

    @BeforeEach
    public void cleanup() {
        sseReportRepository.deleteAll();
    }

    @Test
    public void findCreatedAfter() throws IOException {
        // Data contains 3 sites
        saveNewTlscReports("example-sse-report1.json");
        final Instant firstUpdate = TimeUtil.roundInstantSeconds(getTransactionTimestamp());
        ThreadUtil.delayMs(2100);
        commitAndEndTransactionAndStartNew();
        // Data contains 2 sites
        saveNewTlscReports("example-sse-report2.json");
        final Instant secondUpdate = TimeUtil.roundInstantSeconds(getTransactionTimestamp());

        // Find data updated during both updates (3 pcs) and data updated after first update (2 pcs)
        final SseFeatureCollectionV1 dataUpdatedIncludingFirstUpdate = sseServiceV1.findCreatedAfter(firstUpdate.minusMillis(1));
        final SseFeatureCollectionV1 dataUpdatedAfterFirstUpdate = sseServiceV1.findCreatedAfter(secondUpdate.minusMillis(1));

        assertEquals(5, dataUpdatedIncludingFirstUpdate.getFeatures().size());
        assertEquals(2, dataUpdatedAfterFirstUpdate.getFeatures().size());

        assertSiteNumber(20243, 0, dataUpdatedIncludingFirstUpdate.getFeatures());
        assertSiteNumber(20243, 1, dataUpdatedIncludingFirstUpdate.getFeatures());
        assertSiteNumber(20169, 2, dataUpdatedIncludingFirstUpdate.getFeatures());
        assertSiteNumber(20243, 3, dataUpdatedIncludingFirstUpdate.getFeatures());
        assertSiteNumber(20169, 4, dataUpdatedIncludingFirstUpdate.getFeatures());

        assertSiteNumber(20243, 0, dataUpdatedAfterFirstUpdate.getFeatures());
        assertSiteNumber(20169, 1, dataUpdatedAfterFirstUpdate.getFeatures());
    }


    private void assertSiteNumber(final int expected, final int historyIndex, final List<SseFeatureV1> history) {
        assertEquals(expected, history.get(historyIndex).getSiteNumber());
    }

    private void saveNewTlscReports(final String file) throws IOException {
        final String postJson = readFile("sse/" + file);
        final ObjectReader genericJsonReader = objectMapper.reader();
        final JsonNode json = genericJsonReader.readTree(postJson);
        final List<SseReport> sseReports = convertToSseReports(json);
        sseReports.forEach(sseReport -> {
            sseReportRepository.markSiteLatestReportAsNotLatest(sseReport.getSiteNumber());
            sseReportRepository.save(sseReport);
        });
    }

    private List<SseReport> convertToSseReports(final JsonNode json) {
        final List<SseReport> sseReports = new ArrayList<>();
        final JsonNode sseReportsNode = json.get("SSE_Reports");
        for(int i = 0; i < sseReportsNode.size(); i++) {
            final JsonNode reportNode = sseReportsNode.get(i);
            sseReports.add(convertToSseReport(reportNode));
        }
        return sseReports;
    }

    private static SseReport convertToSseReport(final JsonNode r) {
        final JsonNode site = r.get("Site");
        final JsonNode sseFields = r.get("SSE_Fields");
        final JsonNode extraFields = r.get("Extra_Fields");

        return new SseReport(
            Instant.now(),
            true,
            site.get("SiteNumber").asInt(),
            site.get("SiteName").asText(),
            SsePropertiesV1.SiteType.fromValue(site.get("SiteType").asText()),
            ZonedDateTime.parse(sseFields.get("Last_Update").asText()).toInstant(),
            SsePropertiesV1.SeaState.fromValue(sseFields.get("SeaState").asText()),
            SsePropertiesV1.Trend.fromValue(sseFields.get("Trend").asText()),
            sseFields.get("WindWaveDir").asInt(),
            SsePropertiesV1.Confidence.fromValue(sseFields.get("Confidence").asText()),
            asBigDecimal(extraFields.get("Heel_Angle").asDouble()),
            SsePropertiesV1.LightStatus.fromValue(extraFields.get("Light_Status").asText()),
            extraFields.get("Temperature").asInt(),
            asBigDecimal(extraFields.get("Coord_Longitude").asDouble()),
            asBigDecimal(extraFields.get("Coord_Latitude").asDouble())
        );
    }

    private static BigDecimal asBigDecimal(final Double value) {
        return value != null ? BigDecimal.valueOf(value) : null;
    }
}
