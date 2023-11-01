package fi.livi.digitraffic.meri.dto.sse.v1;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.dto.geojson.Feature;
import fi.livi.digitraffic.meri.dto.geojson.Point;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
        "siteNumber",
        "type",
        "geometry",
        "properties"
})
@Schema(description = "GeoJSON Feature object of sea state estimate (SSE)")
public class SseFeatureV1 extends Feature<Point, SsePropertiesV1> {

    @Schema(description = "Identifier of the site")
    private final int siteNumber;

    public SseFeatureV1(final Point geometry, final SsePropertiesV1 properties, final int siteNumber) {
        super(geometry, properties);
        this.siteNumber = siteNumber;
    }

    public int getSiteNumber() {
        return siteNumber;
    }
}
