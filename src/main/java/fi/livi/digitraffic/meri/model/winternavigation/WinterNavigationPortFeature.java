package fi.livi.digitraffic.meri.model.winternavigation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Feature;
import fi.livi.digitraffic.meri.model.geojson.Point;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({
        "locode",
        "type",
        "geometry",
        "properties"
})
@ApiModel(description = "GeoJSON Feature object")
public class WinterNavigationPortFeature extends Feature<Point, WinterNavigationPortProperties> {

    @ApiModelProperty(value = "Port SafeSeaNet location code", required = true)
    public final String locode;

    public WinterNavigationPortFeature(final String locode, final WinterNavigationPortProperties properties, final Point geometry) {
        super(geometry, properties);
        this.locode = locode;
    }
}
