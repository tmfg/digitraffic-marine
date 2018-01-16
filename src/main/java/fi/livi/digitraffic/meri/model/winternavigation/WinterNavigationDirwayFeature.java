package fi.livi.digitraffic.meri.model.winternavigation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Geometry;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({ "name",
                     "type",
                     "geometry",
                     "properties" })
@ApiModel(description = "GeoJSON Feature object")
public class WinterNavigationDirwayFeature {

    @ApiModelProperty(value = "Name of the dirway", required = true)
    public final String name;

    @ApiModelProperty(value = "GeoJSON Properties object", required = true)
    public final WinterNavigationDirwayProperties properties;

    @ApiModelProperty("GeoJSON Geometry object")
    public final Geometry geometry;

    @ApiModelProperty(allowableValues = "Feature", required = true)
    public final String type = "Feature";

    public WinterNavigationDirwayFeature(final String name,
                                         final WinterNavigationDirwayProperties properties,
                                         final Geometry geometry) {
        this.name = name;
        this.properties = properties;
        this.geometry = geometry;
    }
}
