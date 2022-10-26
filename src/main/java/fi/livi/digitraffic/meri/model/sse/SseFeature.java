package fi.livi.digitraffic.meri.model.sse;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Feature;
import fi.livi.digitraffic.meri.model.geojson.Point;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
        "siteNumber",
        "type",
        "geometry",
        "properties"
})
@Schema(description = "GeoJSON Feature object of sea state estimate (SSE)")
public class SseFeature extends Feature<Point, SseProperties> {

    @Schema(description = "Identifier of the site")
    private final int siteNumber;

    public SseFeature(final Point geometry, final SseProperties properties, final int siteNumber) {
        super(geometry, properties);
        this.siteNumber = siteNumber;
    }

    public int getSiteNumber() {
        return siteNumber;
    }
}
