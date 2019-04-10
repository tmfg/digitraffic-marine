package fi.livi.digitraffic.meri.model.geojson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "GeoJSON Feature object", parent = GeoJsonObject.class)
public abstract class Feature<T extends Geometry, PropertiesType>  extends GeoJsonObject {

    @ApiModelProperty(value = "Type of GeoJSON object", allowableValues = "Feature", required = true)
    private final String type = "Feature";

    @ApiModelProperty(value = "GeoJSON Geometry object", required = true)
    @JsonProperty("geometry")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private T geometry;

    @ApiModelProperty(value = "GeoJSON Properties object", required = true)
    private PropertiesType properties;

    public Feature() {
    }

    public Feature(final PropertiesType properties) {
        this.properties = properties;
    }

    public Feature(final T geometry, final PropertiesType properties) {
        this.geometry = geometry;
        this.properties = properties;
    }

    @Override
    public String getType() {
        return type;
    }

    public T getGeometry() {
        return geometry;
    }

    public void setGeometry(final T geometry) {
        this.geometry = geometry;
    }

    public PropertiesType getProperties() {
        return properties;
    }

    public void setProperties(PropertiesType properties) {
        this.properties = properties;
    }


}
