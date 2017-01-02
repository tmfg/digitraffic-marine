
package fi.livi.digitraffic.meri.model.pooki;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "features"
})
@ApiModel(description = "GeoJSON FeatureCollection object")
public class FeatureCollection extends GeoJsonObject {

    @ApiModelProperty(required = true)
    @JsonProperty("features")
    private List<Feature> features = new ArrayList<Feature>();

    public void setFeatures(List<Feature> features) {
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
