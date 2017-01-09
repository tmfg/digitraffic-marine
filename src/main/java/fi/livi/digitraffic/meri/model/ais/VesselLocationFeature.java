package fi.livi.digitraffic.meri.model.ais;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Geometry;
import fi.livi.digitraffic.meri.model.geojson.Point;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({
        "mmsi",
        "type",
        "geometry",
        "properties"
})
@ApiModel(description = "GeoJSON Feature object")
public class VesselLocationFeature {
    @ApiModelProperty(value = "Maritime Mobile Service Identity", required = true)
    public final int mmsi;

    @ApiModelProperty(value = "GeoJSON Properties object", required = true)
    @JsonProperty("properties")
    public final VesselLocationProperties properties;

    @ApiModelProperty("GeoJSON Geometry object")
    @JsonProperty("geometry")
    public final Geometry geometry;

    @ApiModelProperty(allowableValues = "Feature", required = true)
    public final String type = "Feature";

    public VesselLocationFeature(final int mmsi, final VesselLocationProperties properties, final Point geometry) {
        this.mmsi = mmsi;
        this.properties = properties;
        this.geometry = geometry;
    }
}
