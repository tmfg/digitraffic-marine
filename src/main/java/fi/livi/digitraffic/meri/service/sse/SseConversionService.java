package fi.livi.digitraffic.meri.service.sse;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.SSE_DATA;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.domain.sse.SseReport;
import fi.livi.digitraffic.meri.model.geojson.Point;
import fi.livi.digitraffic.meri.model.sse.SseFeature;
import fi.livi.digitraffic.meri.model.sse.SseFeatureCollection;
import fi.livi.digitraffic.meri.model.sse.SseProperties;

@Service
public class SseConversionService {

    private final UpdatedTimestampRepository updatedTimestampRepository;

    public SseConversionService(final UpdatedTimestampRepository updatedTimestampRepository) {
        this.updatedTimestampRepository = updatedTimestampRepository;
    }

    @Transactional
    public SseFeatureCollection createSseFeatureCollectionFrom(List<SseReport> sseReports) {
        final ZonedDateTime updated = updatedTimestampRepository.findLastUpdated(SSE_DATA);

        final List<SseFeature> features = sseReports.stream().map(r -> createSseFeatureFrom(r)).collect(Collectors.toList());

        return new SseFeatureCollection(updated, features);
    }

    public SseFeature createSseFeatureFrom(final SseReport sseReport) {
        // For fixed AtoNs, only the light status, last update, confidence and temperature fields are usable.
        final boolean floating = sseReport.isFloating();
        final SseProperties sseProperties = new SseProperties(
            sseReport.getSiteNumber(),
            sseReport.getSiteName(),
            sseReport.getSiteType(),
            sseReport.getLastUpdate().withZoneSameInstant(ZoneOffset.UTC),
            floating ? sseReport.getSeaState() : null,
            floating ? sseReport.getTrend() : null,
            floating ? sseReport.getWindWaveDir() : null,
            sseReport.getConfidence(),
            floating ? sseReport.getHeelAngle() : null,
            sseReport.getLightStatus(),
            sseReport.getTemperature());

        final Point point = createPointFrom(sseReport);

        return new SseFeature(point, sseProperties, sseReport.getSiteNumber());
    }

    private Point createPointFrom(SseReport sseReport) {
        if (sseReport.getLongitude() == null || sseReport.getLatitude() == null) {
            return new Point();
        }
        return new Point(sseReport.getLongitude().doubleValue(), sseReport.getLatitude().doubleValue());
    }
}
