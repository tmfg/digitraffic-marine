package fi.livi.digitraffic.meri.service.sse.v1;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.SSE_DATA;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dto.geojson.Point;
import fi.livi.digitraffic.meri.dto.sse.v1.SseFeatureCollectionV1;
import fi.livi.digitraffic.meri.dto.sse.v1.SseFeatureV1;
import fi.livi.digitraffic.meri.dto.sse.v1.SsePropertiesV1;
import fi.livi.digitraffic.meri.model.sse.SseReport;

@Service
public class SseConversionServiceV1 {

    private final UpdatedTimestampRepository updatedTimestampRepository;

    public SseConversionServiceV1(final UpdatedTimestampRepository updatedTimestampRepository) {
        this.updatedTimestampRepository = updatedTimestampRepository;
    }

    @Transactional
    public SseFeatureCollectionV1 createSseFeatureCollectionFrom(final List<SseReport> sseReports) {
        final Instant updated = updatedTimestampRepository.findLastUpdatedInstant(SSE_DATA);

        final List<SseFeatureV1> features = sseReports.stream().map(this::createSseFeatureFrom).collect(Collectors.toList());

        return new SseFeatureCollectionV1(updated, features);
    }

    private SseFeatureV1 createSseFeatureFrom(final SseReport sseReport) {
        // For fixed AtoNs, only the light status, last update, confidence and temperature fields are usable.
        final boolean floating = sseReport.isFloating();
        final SsePropertiesV1 sseProperties = new SsePropertiesV1(
            sseReport.getSiteNumber(),
            sseReport.getSiteName(),
            sseReport.getSiteType(),
            sseReport.getLastUpdate(),
            floating ? sseReport.getSeaState() : null,
            floating ? sseReport.getTrend() : null,
            floating ? sseReport.getWindWaveDir() : null,
            sseReport.getConfidence(),
            floating ? sseReport.getHeelAngle() : null,
            sseReport.getLightStatus(),
            sseReport.getTemperature(),
            sseReport.getCreated());

        final Point point = createPointFrom(sseReport);

        return new SseFeatureV1(point, sseProperties, sseReport.getSiteNumber());
    }

    private Point createPointFrom(final SseReport sseReport) {
        if (sseReport.getLongitude() == null || sseReport.getLatitude() == null) {
            return new Point();
        }
        return new Point(sseReport.getLongitude().doubleValue(), sseReport.getLatitude().doubleValue());
    }
}
