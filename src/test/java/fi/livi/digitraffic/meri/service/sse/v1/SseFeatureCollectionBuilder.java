package fi.livi.digitraffic.meri.service.sse.v1;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import fi.livi.digitraffic.meri.dto.geojson.Point;
import fi.livi.digitraffic.meri.dto.sse.v1.SseFeatureCollectionV1;
import fi.livi.digitraffic.meri.dto.sse.v1.SseFeatureV1;
import fi.livi.digitraffic.meri.dto.sse.v1.SsePropertiesV1;

public class SseFeatureCollectionBuilder {

    private final Instant lastUpdate;

    public SseFeatureCollectionBuilder(final Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public SseFeatureCollectionV1 build() {

        final List<SseFeatureV1> features = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            features.add(new SseFeatureV1(new Point(20.0, 60.0),
                         new SsePropertiesV1(i, "siteName" + i, SsePropertiesV1.SiteType.FLOATING, lastUpdate, SsePropertiesV1.SeaState.STORM,
                                           SsePropertiesV1.Trend.ASCENDING, 90, SsePropertiesV1.Confidence.GOOD, BigDecimal.valueOf(45.0),
                                           SsePropertiesV1.LightStatus.ON, 10, Instant.now()), i) {
            });
        }

        return new SseFeatureCollectionV1(lastUpdate, features);
    }
}
