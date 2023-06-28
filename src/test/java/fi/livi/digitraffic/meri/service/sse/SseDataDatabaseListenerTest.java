package fi.livi.digitraffic.meri.service.sse;

import static fi.livi.digitraffic.meri.model.sse.SseProperties.Confidence;
import static fi.livi.digitraffic.meri.model.sse.SseProperties.LightStatus;
import static fi.livi.digitraffic.meri.model.sse.SseProperties.Trend;
import static fi.livi.digitraffic.meri.service.sse.SseMqttSenderV2.createMqttDataMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.meri.service.MqttRelayQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
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
    @MockBean
    private CachedLocker cachedLocker;

    @MockBean
    private MqttRelayQueue mqttRelayQueue;

    @SpyBean
    private SseMqttSenderV2 sseMqttSenderV2;

    @Autowired
    private SseReportRepository sseReportRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void cleanUp() {
        sseReportRepository.deleteAll();
    }

    @Test
    public void verifyMqttSendMessagesIsCalledFromScheduler() throws IOException {
        when(cachedLocker.hasLock()).thenReturn(true);
        final Instant first = Instant.now().plus(30, ChronoUnit.MINUTES);
        final Instant second = first.plus(10, ChronoUnit.MINUTES);
        final Instant third = second.plus(10, ChronoUnit.MINUTES);

        // First no new reports in db
        triggerSheduledTask();
        verify(mqttRelayQueue, Mockito.never()).queueMqttMessage(any(), any(), any());

        // One new report
        final SseFeatureCollection sse1 = createFeatureCollection(1, first);
        saveReports(sse1);
        // Trigger scheduled run and check the new report is send to mqtt
        triggerSheduledTask();
        verify(mqttRelayQueue, Mockito.times(1)).queueMqttMessage(any(), any(), any());

        // Create two more
        final SseFeatureCollection sse2 = createFeatureCollection(2, second);
        final SseFeatureCollection sse3 = createFeatureCollection(3, third);
        saveReports(sse2);
        saveReports(sse3);
        // Trigger scheduled run
        triggerSheduledTask();
        final ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        // 3 times as 1. is counted also
        verify(mqttRelayQueue, Mockito.times(3)).queueMqttMessage(any(), argumentCaptor.capture(), any());
        final List<String> capturedValues = argumentCaptor.getAllValues();
        assertEquals(3, capturedValues.size());

        // Check the that reports are send to mqtt in right order and they equals with original data
        assertSseFeaturesEquals(sse1.getFeatures().get(0), capturedValues.get(0));
        assertSseFeaturesEquals(sse2.getFeatures().get(0), capturedValues.get(1));
        assertSseFeaturesEquals(sse3.getFeatures().get(0), capturedValues.get(2));
    }

    private void triggerSheduledTask() {
        ReflectionTestUtils.invokeMethod(sseMqttSenderV2,  "checkNewSseReports");
    }

    private void assertSseFeaturesEquals(final SseFeature expected, final String capturedJson) {
        final String expectedJson = StringUtil.toJsonString(createMqttDataMessage(expected).getData());
        assertEquals(expectedJson, capturedJson);
    }

    private SseFeatureCollection createFeatureCollection(final int siteNumber, final Instant created) {
        final SseProperties properties = new SseProperties(
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

    private void saveReports(final SseFeatureCollection sseFeatureCollection) {
        convertToSseReports(sseFeatureCollection).forEach(sseReport -> {
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
