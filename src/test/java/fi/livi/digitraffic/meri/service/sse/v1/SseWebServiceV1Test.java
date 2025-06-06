package fi.livi.digitraffic.meri.service.sse.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import fi.livi.digitraffic.meri.AbstractWebTestBase;
import fi.livi.digitraffic.meri.dao.sse.SseReportRepository;
import fi.livi.digitraffic.meri.dto.sse.v1.SseFeatureCollectionV1;
import fi.livi.digitraffic.meri.dto.sse.v1.SseFeatureV1;
import fi.livi.digitraffic.meri.dto.sse.v1.SsePropertiesV1;
import fi.livi.digitraffic.meri.model.sse.SseReport;
import jakarta.transaction.Transactional;

@Transactional
public class SseWebServiceV1Test extends AbstractWebTestBase {

    private static final Logger log = LoggerFactory.getLogger(SseWebServiceV1Test.class);

    @MockitoSpyBean
    private SseWebServiceV1 sseWebServiceV1;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SseReportRepository sseReportRepository;

    @BeforeEach
    public void cleanup() {
        sseReportRepository.deleteAll();
    }

    @Test
    public void findLatestFromMultipleVersions() throws IOException {

        // First update
        saveNewTlscReports("example-sse-report1.json");
        final SseFeatureCollectionV1 latestFirst = sseWebServiceV1.findMeasurements(null);
        log.info("{}", latestFirst);

        assertEquals(2, latestFirst.getFeatures().size());
        assertEquals("Hattukari", latestFirst.getFeatures().get(0).getProperties().getSiteName());

        // Second update
        saveNewTlscReports("example-sse-report2.json");
        final SseFeatureCollectionV1 latestSecond = sseWebServiceV1.findMeasurements(null);

        assertEquals(2, latestSecond.getFeatures().size());
        assertEquals("Hattukari", latestSecond.getFeatures().get(0).getProperties().getSiteName());

        final SseFeatureV1 kipsiFirst = latestFirst.getFeatures().get(1);
        final SseFeatureV1 kipsiSecond = latestSecond.getFeatures().get(1);

        assertEquals(20243, kipsiFirst.getSiteNumber());
        assertEquals(20243, kipsiSecond.getSiteNumber());

        assertEquals("Kipsi", kipsiFirst.getProperties().getSiteName());
        assertEquals("Kipsi", kipsiSecond.getProperties().getSiteName());

        assertEquals(SsePropertiesV1.SeaState.BREEZE, kipsiFirst.getProperties().getSeaState());
        assertEquals(SsePropertiesV1.SeaState.STORM, kipsiSecond.getProperties().getSeaState());

        assertEquals(SsePropertiesV1.Trend.ASCENDING, kipsiFirst.getProperties().getTrend());
        assertEquals(SsePropertiesV1.Trend.NO_CHANGE, kipsiSecond.getProperties().getTrend());

        assertEquals(SsePropertiesV1.Confidence.MODERATE, kipsiFirst.getProperties().getConfidence());
        assertEquals(SsePropertiesV1.Confidence.GOOD, kipsiSecond.getProperties().getConfidence());

        assertEquals(SsePropertiesV1.LightStatus.OFF, kipsiFirst.getProperties().getLightStatus());
        assertEquals(SsePropertiesV1.LightStatus.ON_D, kipsiSecond.getProperties().getLightStatus());

        assertEquals(119L, kipsiFirst.getProperties().getWindWaveDir().longValue());
        assertEquals(200L, kipsiSecond.getProperties().getWindWaveDir().longValue());

        assertEquals(7.5, kipsiFirst.getProperties().getHeelAngle().doubleValue(), 0.1);
        assertEquals(17.5, kipsiSecond.getProperties().getHeelAngle().doubleValue(), 0.1);

        assertEquals(21.22558, kipsiFirst.getGeometry().getCoordinates().get(0), 0.0001);
        assertEquals(21.32558, kipsiSecond.getGeometry().getCoordinates().get(0), 0.0001);

        assertEquals(59.44507, kipsiFirst.getGeometry().getCoordinates().get(1), 0.0001);
        assertEquals(59.54507, kipsiSecond.getGeometry().getCoordinates().get(1), 0.0001);
    }

    // Last updates for sites
    private static final String SITE_20169_1 = "2019-02-11T12:40:00+03:00";
    private static final String SITE_20169_2 = "2019-02-11T13:05:00+03:00";

    private static final String SITE_20243_1 = "2019-04-11T12:30:00+03:00";
    private static final String SITE_20243_2 = "2019-04-11T12:33:20+03:00";
    private static final String SITE_20243_3 = "2019-04-11T13:00:00+03:00";

    @Test
    public void findLatestBySiteNumber() throws IOException {
        // Some data
        saveNewTlscReports("example-sse-report1.json");
        saveNewTlscReports("example-sse-report2.json");

        // Should include all of site 20243 and none of site 20169
        final List<SseFeatureV1> latest =
            sseWebServiceV1.findMeasurements(20243).getFeatures();

        assertEquals(1, latest.size());
        assertSiteNumber(20243, 0, latest);
        assertLastUpdate(SITE_20243_3, 0, latest);
    }

    @Test
    public void findLatestByNotExistingSiteNumber() throws IOException {
        // Some data
        saveNewTlscReports("example-sse-report1.json");
        saveNewTlscReports("example-sse-report2.json");

        final SseFeatureCollectionV1 latest = sseWebServiceV1.findMeasurements(12345);
        assertEquals(0, latest.getFeatures().size());
    }

    @Test
    public void findHistoryByTimeIncludingStartAndEnd() throws IOException {
        // Some data
        saveNewTlscReports("example-sse-report1.json");
        saveNewTlscReports("example-sse-report2.json");

        // Should include all of site 20243 and none of site 20169
        final List<SseFeatureV1> history =
            sseWebServiceV1.findHistory(null, Instant.parse(SITE_20243_1), Instant.parse(SITE_20243_3)).getFeatures();

        assertEquals(3, history.size());
        assertSiteNumber(20243, 0, history);
        assertSiteNumber(20243, 1, history);
        assertSiteNumber(20243, 2, history);
        assertLastUpdate(SITE_20243_1, 0, history);
        assertLastUpdate(SITE_20243_2, 1, history);
        assertLastUpdate(SITE_20243_3, 2, history);
    }

    @Test
    public void findHistoryByTimeSecondTimeDiff() throws IOException {
        // Some data
        saveNewTlscReports("example-sse-report1.json");
        saveNewTlscReports("example-sse-report2.json");

        // Should include second of site 20169 and first of 20243
        final List<SseFeatureV1> history =
            sseWebServiceV1.findHistory(null, Instant.parse(SITE_20169_1).plusSeconds(1),
                Instant.parse(SITE_20243_2).minusSeconds(1)).getFeatures();
        assertEquals(2, history.size());

        assertSiteNumber(20169, 0, history);
        assertSiteNumber(20243, 1, history);
        assertLastUpdate(SITE_20169_2, 0, history);
        assertLastUpdate(SITE_20243_1, 1, history);
    }

    @Test
    public void findHistoryByTimeAndSiteNumberIncludingStartAndEnd() throws IOException {
        // Some data
        saveNewTlscReports("example-sse-report1.json");
        saveNewTlscReports("example-sse-report2.json");

        // Time span should include all but query filtered with site 20243 -> all of it's
        final List<SseFeatureV1> history =
            sseWebServiceV1.findHistory(20243, Instant.parse(SITE_20169_1),
                Instant.parse(SITE_20243_3)).getFeatures();

        assertEquals(3, history.size());
        assertSiteNumber(20243, 0, history);
        assertSiteNumber(20243, 1, history);
        assertSiteNumber(20243, 2, history);
        assertLastUpdate(SITE_20243_1, 0, history);
        assertLastUpdate(SITE_20243_2, 1, history);
        assertLastUpdate(SITE_20243_3, 2, history);
    }

    @Test
    public void findHistoryByTimeAndSiteNumberSecondTimeDiff() throws IOException {
        // Some data
        saveNewTlscReports("example-sse-report1.json");
        saveNewTlscReports("example-sse-report2.json");

        // Should include second of site 20169
        final List<SseFeatureV1> history =
            sseWebServiceV1.findHistory(20169, Instant.parse(SITE_20169_1).plusSeconds(1),
                Instant.parse(SITE_20243_2).minusSeconds(1)).getFeatures();
        assertEquals(1, history.size());

        assertSiteNumber(20169, 0, history);
        assertLastUpdate(SITE_20169_2, 0, history);
    }

    @Test
    public void findHistoryBySiteNumber() throws IOException {
        // Some data
        saveNewTlscReports("example-sse-report1.json");
        saveNewTlscReports("example-sse-report2.json");

        // Should include second of site 20169
        final List<SseFeatureV1> history =
            sseWebServiceV1.findHistory(20169, null, null).getFeatures();
        assertEquals(2, history.size());

        assertSiteNumber(20169, 0, history);
        assertSiteNumber(20169, 1, history);
        assertLastUpdate(SITE_20169_1, 0, history);
        assertLastUpdate(SITE_20169_2, 1, history);
    }

    @Test
    public void findHistoryByTimeAndNotExistingSiteNumber() throws IOException {
        // Some data
        saveNewTlscReports("example-sse-report1.json");
        saveNewTlscReports("example-sse-report2.json");

        // Should throw IllegalArgumentException as site 12345 not exists
        final List<SseFeatureV1> history =  sseWebServiceV1.findHistory(12345, Instant.parse(SITE_20169_1).plusSeconds(1),
                Instant.parse(SITE_20243_2).minusSeconds(1)).getFeatures();

        assertEquals(0, history.size());
    }
    private void assertSiteNumber(final int expected, final int historyIndex, final List<SseFeatureV1> history) {
        assertEquals(expected, history.get(historyIndex).getSiteNumber());
    }

    private void assertLastUpdate(final String expectedTime, final int historyIndex, final List<SseFeatureV1> history) {
        assertEquals(Instant.parse(expectedTime), history.get(historyIndex).getProperties().getLastUpdate());
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
            Instant.parse(sseFields.get("Last_Update").asText()),
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
