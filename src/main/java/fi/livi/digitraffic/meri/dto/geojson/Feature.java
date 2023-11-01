package fi.livi.digitraffic.meri.dto.geojson;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import fi.livi.digitraffic.meri.dto.LastModifiedSupport;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GeoJSON Feature object")
public abstract class Feature<G extends Geometry, P extends Properties>  extends GeoJsonObject implements LastModifiedSupport {

    @Schema(description = "Type of GeoJSON object", allowableValues = "Feature", requiredMode = Schema.RequiredMode.REQUIRED, example = "Feature")
    public final String type = "Feature";

    @Schema(description = "GeoJSON Geometry object", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("geometry")
    private G geometry;

    @Schema(description = "GeoJSON Properties object", requiredMode = Schema.RequiredMode.REQUIRED)
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

    @Override
    public Instant getLastModified() {
        return properties != null ? properties.getLastModified() : null;
    }
}
