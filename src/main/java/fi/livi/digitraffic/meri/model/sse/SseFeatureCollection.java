package fi.livi.digitraffic.meri.model.sse;

import java.time.Instant;
import java.util.List;

import fi.livi.digitraffic.meri.model.RootDataObjectDto;
import fi.livi.digitraffic.meri.util.TimeUtil;
import io.swagger.annotations.ApiModel;

@ApiModel(description = "GeoJSON FeatureCollection object of SSE data")
public class SseFeatureCollection extends RootDataObjectDto<SseFeature> {

    public SseFeatureCollection(final Instant dataUpdatedTime,
                                final List<SseFeature> features) {
        super(TimeUtil.toZonedDateTime(dataUpdatedTime), features);
    }
}
