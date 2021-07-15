package fi.livi.digitraffic.meri.service.sse;

import static fi.livi.digitraffic.meri.model.sse.SseProperties.Confidence;
import static fi.livi.digitraffic.meri.model.sse.SseProperties.LightStatus;
import static fi.livi.digitraffic.meri.model.sse.SseProperties.Trend;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.controller.CachedLocker;
import fi.livi.digitraffic.meri.dao.sse.SseReportRepository;
import fi.livi.digitraffic.meri.domain.sse.SseReport;
import fi.livi.digitraffic.meri.model.geojson.Point;
import fi.livi.digitraffic.meri.model.sse.SseFeature;
import fi.livi.digitraffic.meri.model.sse.SseFeatureCollection;
import fi.livi.digitraffic.meri.model.sse.SseProperties;
import fi.livi.digitraffic.meri.model.sse.SseProperties.SeaState;
import fi.livi.digitraffic.meri.util.StringUtil;

@Transactional
@TestPropertySource(properties = { "sse.mqtt.enabled=true" })
public class SseDataDatabaseListenerTest extends AbstractTestBase {

    private static final Logger log = LoggerFactory.getLogger(SseDataDatabaseListenerTest.class);

    @MockBean
    private CachedLocker cachedLocker;

    @MockBean
    private SseMqttSender sseMqttSender;

    @SpyBean
    private SseDataDatabaseListener sseDataDatabaseListener;

    @Autowired
    private SseReportRepository sseReportRepository;

    @BeforeEach
    public void cleanUp() {
        sseReportRepository.deleteAll();
    }

    @Test
    public void verifyMqttSendMessagesIsCalledFromScheduler() throws IOException {
        when(cachedLocker.hasLock()).thenReturn(true);
        final Instant first = Instant.now().minus(30, ChronoUnit.MINUTES);
        final Instant second = first.plus(10, ChronoUnit.MINUTES);
        final Instant third = second.plus(10, ChronoUnit.MINUTES);
        ReflectionTestUtils.setField(sseDataDatabaseListener, "latest", first.minusSeconds(1));

        // First no new reports in db
        triggerSheduledTask();
        verify(sseMqttSender, Mockito.never()).sendSseMessage(Mockito.any(SseFeature.class));

        // One new report
        SseFeatureCollection sse1 = createFeatureCollection(1, first);
        saveReports(sse1);
        // Trigger scheduled run and check the new report is send to mqtt
        triggerSheduledTask();
        verify(sseMqttSender, Mockito.times(1)).sendSseMessage(Mockito.any(SseFeature.class));

        // Create two more
        final SseFeatureCollection sse2 = createFeatureCollection(2, second);
        final SseFeatureCollection sse3 = createFeatureCollection(3, third);
        saveReports(sse2);
        saveReports(sse3);
        // Trigger scheduled run
        ReflectionTestUtils.invokeMethod(sseDataDatabaseListener,  "checkNewSseReports");
        final ArgumentCaptor<SseFeature> argumentCaptor = ArgumentCaptor.forClass(SseFeature.class);
        // 3 times as 1. is counted also
        verify(sseMqttSender, Mockito.times(3)).sendSseMessage(argumentCaptor.capture());
        final List<SseFeature> capturedValues = argumentCaptor.getAllValues();
        assertEquals(3, capturedValues.size());

        // Check the that reports are send to mqtt in right order and they equals with original data
        assertSseFeaturesEquals(sse1.getFeatures().get(0), capturedValues.get(0));
        assertSseFeaturesEquals(sse2.getFeatures().get(0), capturedValues.get(1));
        assertSseFeaturesEquals(sse3.getFeatures().get(0), capturedValues.get(2));
    }

    private void triggerSheduledTask() {
        ReflectionTestUtils.invokeMethod(sseDataDatabaseListener,  "checkNewSseReports");
    }

    private void assertSseFeaturesEquals(final SseFeature expected, final SseFeature captured) {
        final String expectedJson = StringUtil.toJsonString(expected);
        final String capturedJson = StringUtil.toJsonString(captured);
        assertEquals(expectedJson, capturedJson);
    }

    private SseFeatureCollection createFeatureCollection(final int siteNumber, final Instant created) {
        SseProperties properties = new SseProperties(
            siteNumber,
            "siteName" + siteNumber,
            SseProperties.SiteType.FLOATING,
            Instant.now().plusSeconds(getRandom(0, 60)),
            SeaState.STORM,
            Trend.NO_CHANGE,
            getRandom(0, 360),
            Confidence.GOOD,
            BigDecimal.valueOf(getRandom(0, 90)),
            LightStatus.ON,
            getRandom(-30, 30),
            created
        );
        final SseFeature f = new SseFeature(new Point(21.33210, 54.55432), properties, siteNumber);
        return new SseFeatureCollection(Instant.now(), Collections.singletonList(f));
    }

    private void saveReports(final SseFeatureCollection sseFeatureCollection) throws IOException {
        List<SseReport> sseReports = convertToSseReports(sseFeatureCollection);
        sseReports.forEach(sseReport -> {
            sseReportRepository.markSiteLatestReportAsNotLatest(sseReport.getSiteNumber());
            sseReportRepository.save(sseReport);
        });
    }

    private List<SseReport> convertToSseReports(final SseFeatureCollection fc) {
        return fc.getFeatures().stream().map(f -> convertToSseReport(f)).collect(Collectors.toList());
    }

    private static SseReport convertToSseReport(final SseFeature f) {
        final SseProperties p = f.getProperties();
        return new SseReport(
            f.getProperties().getCreated(),
            true,
            p.getSiteNumber(),
            p.getSiteName(),
            p.getSiteType(),
            p.getLastUpdate(),
            p.getSeaState(),
            p.getTrend(),
            p.getWindWaveDir(),
            p.getConfidence(),
            p.getHeelAngle(),
            p.getLightStatus(),
            p.getTemperature(),
            asBigDecimal(f.getGeometry().getCoordinates().get(0)),
            asBigDecimal(f.getGeometry().getCoordinates().get(1))
        );
    }

    private static BigDecimal asBigDecimal(final Double value) {
        return value != null ? BigDecimal.valueOf(value) : null;
    }
}
