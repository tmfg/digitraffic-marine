package fi.livi.digitraffic.meri.service.sse;

import static fi.livi.digitraffic.meri.model.sse.SseProperties.Confidence;
import static fi.livi.digitraffic.meri.model.sse.SseProperties.LightStatus;
import static fi.livi.digitraffic.meri.model.sse.SseProperties.Trend;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.io.IOException;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("conversionService")
    private ConversionService conversionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    @Rollback
    @Test
    public void handleUnhandledSseReports() throws IOException {

        // First update
        saveNewTlscReports("example-sse-report1.json");
        final int handledFirst = sseService.handleUnhandledSseReports(10);
        SseFeatureCollection latestFirst = sseService.findLatest();
        log.info("{}", latestFirst);

        Assert.assertEquals(2, latestFirst.getFeatures().size());
        Assert.assertEquals("Hattukari", latestFirst.getFeatures().get(0).getProperties().getSiteName());

        // Second update
        saveNewTlscReports("example-sse-report2.json");
        final int handledSecond = sseService.handleUnhandledSseReports(10);
        SseFeatureCollection latestSecond = sseService.findLatest();

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

    private void saveNewTlscReports(final String file) throws IOException {
        final String postJson = readFile("sse/" + file);
        final TlscSseReports postObject = objectMapper.readerFor(TlscSseReports.class).readValue(postJson);
        sseService.saveTlscSseReports(postObject);
    }

    private void verifyConverterTimes(final int times, Class<?> sourceType, Class<?> targetType) {
        Mockito.verify(conversionService, Mockito.times(times))
            .convert(any(sourceType), eq(targetType));
    }
}
