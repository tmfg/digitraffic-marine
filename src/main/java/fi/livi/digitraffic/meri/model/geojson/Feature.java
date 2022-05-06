package fi.livi.digitraffic.meri.model.geojson;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GeoJSON Feature object")
public abstract class Feature<G extends Geometry, P extends Properties>  extends GeoJsonObject {

    @Schema(description = "Type of GeoJSON object", allowableValues = "Feature", required = true, example = "Feature")
    private final String type = "Feature";

    @Schema(description = "GeoJSON Geometry object", required = true)
    @JsonProperty("geometry")
    private G geometry;

    @Schema(description = "GeoJSON Properties object", required = true)
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
