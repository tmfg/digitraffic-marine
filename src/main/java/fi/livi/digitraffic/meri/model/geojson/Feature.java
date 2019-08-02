package fi.livi.digitraffic.meri.model.geojson;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "GeoJSON Feature object")
public abstract class Feature<G extends Geometry, P extends Properties>  extends GeoJsonObject {

    @ApiModelProperty(value = "Type of GeoJSON object", allowableValues = "Feature", required = true, example = "Feature")
    private final String type = "Feature";

    @ApiModelProperty(value = "GeoJSON Geometry object", required = true)
    @JsonProperty("geometry")
    private G geometry;

    @ApiModelProperty(value = "GeoJSON Properties object", required = true)
    private P properties;

    public Feature() {
    }

    public Feature(final P properties) {
        this.properties = properties;
    }

    public Feature(final G geometry, final P properties) {
        this.geometry = geometry;
        this.properties = properties;
    }

    @Override
    public String getType() {
        return type;
    }

    public G getGeometry() {
        return geometry;
    }

    public void setGeometry(final G geometry) {
        this.geometry = geometry;
    }

    public P getProperties() {
        return properties;
    }

    public void setProperties(final P properties) {
        this.properties = properties;
    }
}
