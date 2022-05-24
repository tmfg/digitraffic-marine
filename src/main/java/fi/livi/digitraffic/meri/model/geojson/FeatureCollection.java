package fi.livi.digitraffic.meri.model.geojson;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "type", "features" })
public class FeatureCollection<FeatureType> extends GeoJsonObject {

    @Schema(description = "Type of GeoJSON object", allowableValues = "FeatureCollection", required = true)
    private final String type = "FeatureCollection";

    @Schema(required = true)
    @JsonProperty("features")
    private List<FeatureType> features = new ArrayList<>();

    public FeatureCollection() {
    }

    public FeatureCollection(final List<FeatureType> features) {
        this.features = features;
    }


    @Override
    public String getType() {
        return type;
    }

    public void setFeatures(final List<FeatureType> features) {
        this.features = features;
    }

    public List<FeatureType> getFeatures() {
        return features;
    }
}
