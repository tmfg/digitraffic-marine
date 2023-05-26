package fi.livi.digitraffic.meri.model.winternavigation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Feature;
import fi.livi.digitraffic.meri.model.geojson.Point;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
        "locode",
        "type",
        "geometry",
        "properties"
})
public class WinterNavigationPortFeature extends Feature<Point, WinterNavigationPortProperties> {

    @Schema(description = "Port SafeSeaNet location code", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String locode;

    public WinterNavigationPortFeature(final String locode, final WinterNavigationPortProperties properties, final Point geometry) {
        super(geometry, properties);
        this.locode = locode;
    }
}
