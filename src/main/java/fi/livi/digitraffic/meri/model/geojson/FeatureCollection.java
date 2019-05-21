package fi.livi.digitraffic.meri.model.geojson;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class FeatureCollection<FeatureType> extends GeoJsonObject {

    @ApiModelProperty(value = "Type of GeoJSON object", allowableValues = "FeatureCollection", required = true)
    private final String type = "FeatureCollection";

    @ApiModelProperty(required = true)
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
