package fi.livi.digitraffic.meri.model.winternavigation;

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

public class WinterNavigationPortFeatureCollection {

    @ApiModelProperty(required = true)
    public final List<WinterNavigationPortFeature> features;

    @ApiModelProperty(allowableValues = "FeatureCollection", required = true)
    public final String type = "FeatureCollection";

    public WinterNavigationPortFeatureCollection(final List<WinterNavigationPortFeature> features) {
        this.features = features;
    }
}
