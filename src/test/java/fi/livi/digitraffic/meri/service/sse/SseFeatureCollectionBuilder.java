package fi.livi.digitraffic.meri.service.sse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import fi.livi.digitraffic.meri.model.geojson.Point;
import fi.livi.digitraffic.meri.model.sse.SseFeature;
import fi.livi.digitraffic.meri.model.sse.SseFeatureCollection;
import fi.livi.digitraffic.meri.model.sse.SseProperties;

public class SseFeatureCollectionBuilder {

    private final Instant lastUpdate;

    public SseFeatureCollectionBuilder(final Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public SseFeatureCollection build() {

        final List<SseFeature> features = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            features.add(new SseFeature(new Point(20.0, 60.0),
                         new SseProperties(i, "siteName" + i, SseProperties.SiteType.FLOATING, lastUpdate, SseProperties.SeaState.STORM,
                                           SseProperties.Trend.ASCENDING, 90, SseProperties.Confidence.GOOD, BigDecimal.valueOf(45.0),
                                           SseProperties.LightStatus.ON, 10, Instant.now()), i) {
            });
        }

        return new SseFeatureCollection(lastUpdate, features);
    }
}
