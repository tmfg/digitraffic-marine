package fi.livi.digitraffic.meri.model.portnet.metadata;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Geometry;
import fi.livi.digitraffic.meri.model.geojson.Point;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({
        "locode",
        "type",
        "geometry",
        "properties"
})
@ApiModel(description = "GeoJSON SafeSeaNet Location Feature object")
public class SsnLocationFeature {
    @ApiModelProperty(value = "Maritime Mobile Service Identity", required = true)
    public final String locode;

    @ApiModelProperty(value = "GeoJSON Properties object", required = true)
    public final SsnLocationProperties properties;

    @ApiModelProperty("GeoJSON Geometry object")
    public final Geometry geometry;

    @ApiModelProperty(allowableValues = "Feature", required = true)
    public final String type = "Feature";

    public SsnLocationFeature(final String locode, final SsnLocationProperties properties, final Point geometry) {
        this.locode = locode;
        this.properties = properties;
        this.geometry = geometry;
    }

}
