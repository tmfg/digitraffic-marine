package fi.livi.digitraffic.meri.model.geojson;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.dto.data.v1.DataUpdatedSupportV1;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "type", "dataUpdatedTime", "features" })
public class FeatureCollection<FeatureType> extends GeoJsonObject implements DataUpdatedSupportV1 {

    @Schema(description = "Type of GeoJSON object", allowableValues = "FeatureCollection", required = true)
    private final String type = "FeatureCollection";

    @Schema(required = true)
    @JsonProperty("features")
    private List<FeatureType> features = new ArrayList<>();

    @Schema(description = "Data last updated timestamp in ISO 8601 format with time offsets Z (eg. 2022-11-09T09:41:09Z)")
    private Instant dataUpdatedTime;

    public FeatureCollection(final Instant dataUpdatedTime) {
        this.dataUpdatedTime = dataUpdatedTime;
    }

    public FeatureCollection(final List<FeatureType> features, final Instant dataUpdatedTime) {
        this.features = features;
        this.dataUpdatedTime = dataUpdatedTime;
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

    @Override
    public Instant getDataUpdatedTime() {
        return dataUpdatedTime;
    }

    public void setDataUpdatedTime(final Instant dataUpdatedTime) {
        this.dataUpdatedTime = dataUpdatedTime;
    }
}
