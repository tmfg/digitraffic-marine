package fi.livi.digitraffic.meri.model.sse;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Feature;
import fi.livi.digitraffic.meri.model.geojson.Point;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({
        "siteNumber",
        "type",
        "geometry",
        "properties"
})
public class SseFeature extends Feature<Point, SseProperties> {

    @ApiModelProperty(value = "Identifier of the site", position = 1)
    private final int siteNumber;

    public SseFeature(final Point geometry, final SseProperties properties, final int siteNumber) {
        super(geometry, properties);
        this.siteNumber = siteNumber;
    }

    public int getSiteNumber() {
        return siteNumber;
    }
}
