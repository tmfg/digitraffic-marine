package fi.livi.digitraffic.meri.dto.geojson;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.common.dto.LastModifiedSupport;
import fi.livi.digitraffic.common.dto.data.v1.DataUpdatedSupportV1;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "type", "dataUpdatedTime", "features" })
public class FeatureCollection<FeatureType extends LastModifiedSupport> extends GeoJsonObject implements DataUpdatedSupportV1 {

    @Schema(description = "Type of GeoJSON object", allowableValues = "FeatureCollection", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String type = "FeatureCollection";

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("features")
    private List<FeatureType> features = new ArrayList<>();

    @Schema(description = "Data last updated timestamp in ISO 8601 format with time offsets Z (eg. 2022-11-09T09:41:09Z)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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

    public void setDataUpdatedTime(final Instant dataUpdatedTime) {
        this.dataUpdatedTime = dataUpdatedTime;
    }

    public List<FeatureType> getFeatures() {
        return features;
    }

    @Override
    public Instant getDataUpdatedTime() { // I.e. PookiFeatureCollection don't have dataUpdatedTime, so get it from the features
        if (dataUpdatedTime == null && features != null && !features.isEmpty()) {
            try {
                return features.stream().filter(f -> f.getLastModified() != null).map(f -> f.getLastModified()).max(Comparator.comparing(
                        Function.identity())).orElse(null);
            } catch (final Exception e) {
                return null;
            }
        }
        return dataUpdatedTime;
    }
}
