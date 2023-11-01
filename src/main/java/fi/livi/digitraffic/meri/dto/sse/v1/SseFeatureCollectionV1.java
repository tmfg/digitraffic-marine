package fi.livi.digitraffic.meri.dto.sse.v1;

import java.time.Instant;
import java.util.List;

import fi.livi.digitraffic.meri.dto.RootDataObjectDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GeoJSON FeatureCollection object of sea state estimate (SSE)")
public class SseFeatureCollectionV1 extends RootDataObjectDto<SseFeatureV1> {

    public SseFeatureCollectionV1(final Instant dataUpdatedTime,
                                  final List<SseFeatureV1> features) {
        super(dataUpdatedTime, features);
    }
}
