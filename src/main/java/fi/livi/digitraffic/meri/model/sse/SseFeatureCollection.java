package fi.livi.digitraffic.meri.model.sse;

import java.time.Instant;
import java.util.List;

import fi.livi.digitraffic.meri.model.RootDataObjectDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GeoJSON FeatureCollection object of SSE data")
public class SseFeatureCollection extends RootDataObjectDto<SseFeature> {

    public SseFeatureCollection(final Instant dataUpdatedTime,
                                final List<SseFeature> features) {
        super(dataUpdatedTime, features);
    }
}
