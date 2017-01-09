package fi.livi.digitraffic.meri.model.ais;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "GeoJSON FeatureCollection object")
public class VesselLocationFeatureCollection {
    @ApiModelProperty(required = true)
    @JsonProperty("features")
    public final List<VesselLocationFeature> features;

    @ApiModelProperty(allowableValues = "FeatureCollection", required = true)
    public final String type = "FeatureCollection";

    public VesselLocationFeatureCollection(final List<VesselLocationFeature> features) {
        this.features = features;
    }
}
