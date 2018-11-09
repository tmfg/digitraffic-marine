
package fi.livi.digitraffic.meri.model.pooki;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.GeoJsonObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({
    "type",
    "features"
})
@ApiModel(description = "GeoJSON FeatureCollection object")
public class PookiFeatureCollection extends GeoJsonObject {

    @ApiModelProperty(required = true)
    @JsonProperty("features")
    private List<PookiFeature> features = new ArrayList<>();

    public void setFeatures(final List<PookiFeature> features) {
        this.features = features;
    }

    @ApiModelProperty(allowableValues = "FeatureCollection", required = true)
    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
