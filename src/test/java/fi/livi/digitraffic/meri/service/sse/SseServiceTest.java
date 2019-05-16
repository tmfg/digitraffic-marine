package fi.livi.digitraffic.meri.service.sse;

import static fi.livi.digitraffic.meri.model.sse.SseProperties.Confidence;
import static fi.livi.digitraffic.meri.model.sse.SseProperties.LightStatus;
import static fi.livi.digitraffic.meri.model.sse.SseProperties.Trend;
import static java.time.ZoneOffset.UTC;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.annotation.Rollback;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.controller.MessageConverter;
import fi.livi.digitraffic.meri.external.tlsc.sse.TlscSseReports;
import fi.livi.digitraffic.meri.model.sse.SseFeature;
import fi.livi.digitraffic.meri.model.sse.SseFeatureCollection;
import fi.livi.digitraffic.meri.model.sse.SseProperties.SeaState;

@Transactional
public class SseServiceTest extends AbstractTestBase {

    private static final Logger log = LoggerFactory.getLogger(MessageConverter.class);

    @SpyBean
    private SseService sseService;

    @SpyBean
    private SseUpdateService sseUpdateService;

    @SpyBean
    @Qualifier("conversionService")
    private ConversionService conversionService;

    @MockBean // Lazy in service and not initialized in tests
    private SseDataListener sseDataListener;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    @Rollback
    @Test
    public void handleUnhandledSseReports() throws IOException {

        // First update
        saveNewTlscReports("example-sse-report1.json");
        final int handledFirst = sseUpdateService.handleUnhandledSseReports(10);
        SseFeatureCollection latestFirst = sseService.findLatest();
        log.info("{}", latestFirst);

        Assert.assertEquals(2, latestFirst.getFeatures().size());
        Assert.assertEquals("Hattukari", latestFirst.getFeatures().get(0).getProperties().getSiteName());

        // Second update
        saveNewTlscReports("example-sse-report2.json");
        final int handledSecond = sseUpdateService.handleUnhandledSseReports(10);
        SseFeatureCollection latestSecond = sseService.findLatest();

        Assert.assertEquals(3, handledFirst);
        Assert.assertEquals(2, handledSecond);

        Assert.assertEquals(2, latestSecond.getFeatures().size());
        Assert.assertEquals("Hattukari", latestSecond.getFeatures().get(0).getProperties().getSiteName());

        final SseFeature kipsiFirst = latestFirst.getFeatures().get(1);
        final SseFeature kipsiSecond = latestSecond.getFeatures().get(1);

        Assert.assertEquals(20243, kipsiFirst.getSiteNumber());
        Assert.assertEquals(20243, kipsiSecond.getSiteNumber());

        Assert.assertEquals("Kipsi", kipsiFirst.getProperties().getSiteName());
        Assert.assertEquals("Kipsi", kipsiSecond.getProperties().getSiteName());

        Assert.assertEquals(SeaState.BREEZE, kipsiFirst.getProperties().getSeaState());
        Assert.assertEquals(SeaState.STORM, kipsiSecond.getProperties().getSeaState());

        Assert.assertEquals(Trend.ASCENDING, kipsiFirst.getProperties().getTrend());
        Assert.assertEquals(Trend.NO_CHANGE, kipsiSecond.getProperties().getTrend());

        Assert.assertEquals(Confidence.MODERATE, kipsiFirst.getProperties().getConfidence());
        Assert.assertEquals(Confidence.GOOD, kipsiSecond.getProperties().getConfidence());

        Assert.assertEquals(LightStatus.OFF, kipsiFirst.getProperties().getLightStatus());
        Assert.assertEquals(LightStatus.ON_D, kipsiSecond.getProperties().getLightStatus());

        Assert.assertEquals(119L, kipsiFirst.getProperties().getWindWaveDir().longValue());
        Assert.assertEquals(200L, kipsiSecond.getProperties().getWindWaveDir().longValue());

        Assert.assertEquals(7.5, kipsiFirst.getProperties().getHeelAngle().doubleValue(), 0.1);
        Assert.assertEquals(17.5, kipsiSecond.getProperties().getHeelAngle().doubleValue(), 0.1);

        Assert.assertEquals(21.22558, kipsiFirst.getGeometry().getCoordinates().get(0), 0.0001);
        Assert.assertEquals(21.32558, kipsiSecond.getGeometry().getCoordinates().get(0), 0.0001);

        Assert.assertEquals(59.44507, kipsiFirst.getGeometry().getCoordinates().get(1), 0.0001);
        Assert.assertEquals(59.54507, kipsiSecond.getGeometry().getCoordinates().get(1), 0.0001);
    }

    // Last updates for sites
    private static String SITE_20169_1 = "2019-02-11T12:40:00+03:00";
    private static String SITE_20169_2 = "2019-02-11T13:05:00+03:00";

    private static String SITE_20243_1 = "2019-04-11T12:30:00+03:00";
    private static String SITE_20243_2 = "2019-04-11T12:33:20+03:00";
    private static String SITE_20243_3 = "2019-04-11T13:00:00+03:00";

    @Transactional
    @Rollback
    @Test
    public void findLatestBySiteNumber() throws IOException {
        // Some data
        saveNewTlscReports("example-sse-report1.json");
        saveNewTlscReports("example-sse-report2.json");
        sseUpdateService.handleUnhandledSseReports(500);

        // Should include all of site 20243 and none of site 20169
        final List<SseFeature> latest =
            sseService.findLatest(20243).getFeatures();

        Assert.assertEquals(1, latest.size());
        assertSiteNumber(20243, 0, latest);
        assertLastUpdate(SITE_20243_3, 0, latest);
    }

    @Transactional
    @Rollback
    @Test(expected = IllegalArgumentException.class)
    public void findLatestByNotExistingSiteNumber() throws IOException {
        // Some data
        saveNewTlscReports("example-sse-report1.json");
        saveNewTlscReports("example-sse-report2.json");
        sseUpdateService.handleUnhandledSseReports(500);

        // Should throw IllegalArgumentException as site 12345 not exists
        sseService.findLatest(12345);
        Assert.fail("IllegalArgumentException should have been thrown");
    }

    @Transactional
    @Rollback
    @Test
    public void findHistoryByTimeIncludingStartAndEnd() throws IOException {
        // Some data
        saveNewTlscReports("example-sse-report1.json");
        saveNewTlscReports("example-sse-report2.json");
        sseUpdateService.handleUnhandledSseReports(500);

        // Should include all of site 20243 and none of site 20169
        final List<SseFeature> history =
            sseService.findHistory(ZonedDateTime.parse(SITE_20243_1), ZonedDateTime.parse(SITE_20243_3)).getFeatures();

        Assert.assertEquals(3, history.size());
        assertSiteNumber(20243, 0, history);
        assertSiteNumber(20243, 1, history);
        assertSiteNumber(20243, 2, history);
        assertLastUpdate(SITE_20243_1, 0, history);
        assertLastUpdate(SITE_20243_2, 1, history);
        assertLastUpdate(SITE_20243_3, 2, history);
    }

    @Transactional
    @Rollback
    @Test
    public void findHistoryByTimeSecondTimeDiff() throws IOException {
        // Some data
        saveNewTlscReports("example-sse-report1.json");
        saveNewTlscReports("example-sse-report2.json");
        sseUpdateService.handleUnhandledSseReports(500);

        // Should include second of site 20169 and first of 20243
        final List<SseFeature> history =
            sseService.findHistory(ZonedDateTime.parse(SITE_20169_1).plusSeconds(1), ZonedDateTime.parse(SITE_20243_2).minusSeconds(1)).getFeatures();
        Assert.assertEquals(2, history.size());

        assertSiteNumber(20169, 0, history);
        assertSiteNumber(20243, 1, history);
        assertLastUpdate(SITE_20169_2, 0, history);
        assertLastUpdate(SITE_20243_1, 1, history);
    }

    @Transactional
    @Rollback
    @Test
    public void findHistoryByTimeAndSiteNumberIncludingStartAndEnd() throws IOException {
        // Some data
        saveNewTlscReports("example-sse-report1.json");
        saveNewTlscReports("example-sse-report2.json");
        sseUpdateService.handleUnhandledSseReports(500);

        // Time span should include all but query filtered with site 20243 -> all of it's
        final List<SseFeature> history =
            sseService.findHistory(20243, ZonedDateTime.parse(SITE_20169_1), ZonedDateTime.parse(SITE_20243_3)).getFeatures();

        Assert.assertEquals(3, history.size());
        assertSiteNumber(20243, 0, history);
        assertSiteNumber(20243, 1, history);
        assertSiteNumber(20243, 2, history);
        assertLastUpdate(SITE_20243_1, 0, history);
        assertLastUpdate(SITE_20243_2, 1, history);
        assertLastUpdate(SITE_20243_3, 2, history);
    }

    @Transactional
    @Rollback
    @Test
    public void findHistoryByTimeAndSiteNumberSecondTimeDiff() throws IOException {
        // Some data
        saveNewTlscReports("example-sse-report1.json");
        saveNewTlscReports("example-sse-report2.json");
        sseUpdateService.handleUnhandledSseReports(500);

        // Should include second of site 20169
        final List<SseFeature> history =
            sseService.findHistory(20169, ZonedDateTime.parse(SITE_20169_1).plusSeconds(1), ZonedDateTime.parse(SITE_20243_2).minusSeconds(1)).getFeatures();
        Assert.assertEquals(1, history.size());

        assertSiteNumber(20169, 0, history);
        assertLastUpdate(SITE_20169_2, 0, history);
    }

    @Transactional
    @Rollback
    @Test
    public void findHistoryBySiteNumber() throws IOException {
        // Some data
        saveNewTlscReports("example-sse-report1.json");
        saveNewTlscReports("example-sse-report2.json");
        sseUpdateService.handleUnhandledSseReports(500);

        // Should include second of site 20169
        final List<SseFeature> history =
            sseService.findHistory(20169, null, null).getFeatures();
        Assert.assertEquals(2, history.size());

        assertSiteNumber(20169, 0, history);
        assertSiteNumber(20169, 1, history);
        assertLastUpdate(SITE_20169_1, 0, history);
        assertLastUpdate(SITE_20169_2, 1, history);
    }

    @Transactional
    @Rollback
    @Test(expected = IllegalArgumentException.class)
    public void findHistoryByTimeAndNotExistingSiteNumber() throws IOException {
        // Some data
        saveNewTlscReports("example-sse-report1.json");
        saveNewTlscReports("example-sse-report2.json");
        sseUpdateService.handleUnhandledSseReports(500);

        // Should throw IllegalArgumentException as site 12345 not exists
        sseService.findHistory(12345, ZonedDateTime.parse(SITE_20169_1).plusSeconds(1), ZonedDateTime.parse(SITE_20243_2).minusSeconds(1)).getFeatures();
        Assert.fail("IllegalArgumentException should have been thrown");
    }
    private void assertSiteNumber(final int expected, final int historyIndex, final List<SseFeature> history) {
        Assert.assertEquals(expected, history.get(historyIndex).getSiteNumber());
    }

    private void assertLastUpdate(final String expectedTime, int historyIndex, final List<SseFeature> history) {
        Assert.assertEquals(ZonedDateTime.parse(expectedTime).toInstant().atZone(UTC), history.get(historyIndex).getProperties().getLastUpdate());
    }

    private void saveNewTlscReports(final String file) throws IOException {
        final String postJson = readFile("sse/" + file);
        final TlscSseReports postObject = objectMapper.readerFor(TlscSseReports.class).readValue(postJson);
        sseService.saveTlscSseReports(postObject);
    }
}
