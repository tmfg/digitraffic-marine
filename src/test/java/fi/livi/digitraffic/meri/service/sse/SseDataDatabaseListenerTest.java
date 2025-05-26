package fi.livi.digitraffic.meri.service.sse;

import static fi.livi.digitraffic.meri.dto.sse.v1.SsePropertiesV1.Confidence;
import static fi.livi.digitraffic.meri.dto.sse.v1.SsePropertiesV1.LightStatus;
import static fi.livi.digitraffic.meri.dto.sse.v1.SsePropertiesV1.Trend;
import static fi.livi.digitraffic.meri.service.sse.SseMqttSenderV2.createMqttDataMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.livi.digitraffic.common.service.locking.CachedLockingService;
import fi.livi.digitraffic.common.util.StringUtil;
import fi.livi.digitraffic.meri.AbstractDaemonTestBase;
import fi.livi.digitraffic.meri.dao.sse.SseReportRepository;
import fi.livi.digitraffic.meri.dto.geojson.Point;
import fi.livi.digitraffic.meri.dto.sse.v1.SseFeatureCollectionV1;
import fi.livi.digitraffic.meri.dto.sse.v1.SseFeatureV1;
import fi.livi.digitraffic.meri.dto.sse.v1.SsePropertiesV1;
import fi.livi.digitraffic.meri.dto.sse.v1.SsePropertiesV1.SeaState;
import fi.livi.digitraffic.meri.model.sse.SseReport;
import fi.livi.digitraffic.meri.service.MqttRelayQueue;
import jakarta.transaction.Transactional;

@Transactional
@TestPropertySource(properties = { "sse.mqtt.enabled=true" })
public class SseDataDatabaseListenerTest extends AbstractDaemonTestBase {
    @MockitoBean
    private CachedLockingService cachedLocker;

    @MockitoBean
    private MqttRelayQueue mqttRelayQueue;

    @MockitoSpyBean
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
    public void verifyMqttSendMessagesIsCalledFromScheduler() {
        when(cachedLocker.hasLock()).thenReturn(true);
        final Instant first = Instant.now().plus(30, ChronoUnit.MINUTES);
        final Instant second = first.plus(10, ChronoUnit.MINUTES);
        final Instant third = second.plus(10, ChronoUnit.MINUTES);

        // First no new reports in db
        triggerSheduledTask();
        verify(mqttRelayQueue, Mockito.never()).queueMqttMessage(any(), any(), any());

        // One new report
        final SseFeatureCollectionV1 sse1 = createFeatureCollection(1, first);
        saveReports(sse1);
        // Trigger scheduled run and check the new report is send to mqtt
        triggerSheduledTask();
        verify(mqttRelayQueue, Mockito.times(1)).queueMqttMessage(any(), any(), any());

        // Create two more
        final SseFeatureCollectionV1 sse2 = createFeatureCollection(2, second);
        final SseFeatureCollectionV1 sse3 = createFeatureCollection(3, third);
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

    private void assertSseFeaturesEquals(final SseFeatureV1 expected, final String capturedJson) {
        final String expectedJson = StringUtil.toJsonString(createMqttDataMessage(expected).getData());
        assertEquals(expectedJson, capturedJson);
    }

    private SseFeatureCollectionV1 createFeatureCollection(final int siteNumber, final Instant created) {
        final SsePropertiesV1 properties = new SsePropertiesV1(
            siteNumber,
            "siteName" + siteNumber,
            SsePropertiesV1.SiteType.FLOATING,
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
        final SseFeatureV1 f = new SseFeatureV1(new Point(21.33210, 54.55432), properties, siteNumber);
        return new SseFeatureCollectionV1(Instant.now(), Collections.singletonList(f));
    }

    private void saveReports(final SseFeatureCollectionV1 sseFeatureCollectionV1) {
        convertToSseReports(sseFeatureCollectionV1).forEach(sseReport -> {
            sseReportRepository.markSiteLatestReportAsNotLatest(sseReport.getSiteNumber());
            sseReportRepository.save(sseReport);
        });
    }

    private List<SseReport> convertToSseReports(final SseFeatureCollectionV1 fc) {
        return fc.getFeatures().stream().map(SseDataDatabaseListenerTest::convertToSseReport).collect(Collectors.toList());
    }

    private static SseReport convertToSseReport(final SseFeatureV1 f) {
        final SsePropertiesV1 p = f.getProperties();
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
