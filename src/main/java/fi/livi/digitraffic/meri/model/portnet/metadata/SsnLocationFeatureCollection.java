package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({
        "type",
        "dateUpdatedTime",
        "features",
})
@ApiModel(description = "GeoJSON FeatureCollection object")
public class SsnLocationFeatureCollection {
    @ApiModelProperty(required = true)
    public final List<SsnLocationFeature> features;

    @ApiModelProperty(allowableValues = "FeatureCollection", required = true)
    public final String type = "FeatureCollection";

    public SsnLocationFeatureCollection(final List<SsnLocationFeature> ssnLocationFeatures) {
        this.features = ssnLocationFeatures;
    }
}
