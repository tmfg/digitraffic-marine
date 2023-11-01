package fi.livi.digitraffic.meri.dto.winternavigation.v1;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.dto.geojson.Feature;
import fi.livi.digitraffic.meri.dto.geojson.Point;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
        "locode",
        "type",
        "geometry",
        "properties"
})
public class WinterNavigationPortFeatureV1 extends Feature<Point, WinterNavigationPortPropertiesV1> {

    @Schema(description = "Port SafeSeaNet location code", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String locode;

    public WinterNavigationPortFeatureV1(final String locode, final WinterNavigationPortPropertiesV1 properties, final Point geometry) {
        super(geometry, properties);
        this.locode = locode;
    }
}
