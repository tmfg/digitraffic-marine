package fi.livi.digitraffic.meri.model.sse;

import java.time.ZonedDateTime;
import java.util.List;

import fi.livi.digitraffic.meri.model.RootDataObjectDto;
import io.swagger.annotations.ApiModel;

@ApiModel(description = "GeoJSON FeatureCollection object of SSE data")
public class SseFeatureCollection extends RootDataObjectDto<SseFeature> {

    public SseFeatureCollection(final ZonedDateTime dataUpdatedTime,
                                final List<SseFeature> features) {
        super(dataUpdatedTime, features);
    }
}
