package fi.livi.digitraffic.meri.model.winternavigation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Geometry;
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
public class WinterNavigationPortFeature {

    @ApiModelProperty(value = "Maritime Mobile Service Identity", required = true)
    public final String locode;

    @ApiModelProperty(value = "GeoJSON Properties object", required = true)
    public final WinterNavigationPortProperties properties;

    @ApiModelProperty("GeoJSON Geometry object")
    public final Geometry geometry;

    @ApiModelProperty(allowableValues = "Feature", required = true)
    public final String type = "Feature";

    public WinterNavigationPortFeature(final String locode, final WinterNavigationPortProperties properties, final Point geometry) {
        this.locode = locode;
        this.properties = properties;
        this.geometry = geometry;
    }
}
