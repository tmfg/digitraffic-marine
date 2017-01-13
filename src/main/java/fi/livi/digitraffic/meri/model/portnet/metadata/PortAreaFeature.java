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
@ApiModel(description = "GeoJSON Feature object")
public class PortAreaFeature {
    @ApiModelProperty(value = "Maritime Mobile Service Identity", required = true)
    public final String locode;

    @ApiModelProperty(value = "Port area code", required = true)
    public final String portAreaCode;

    @ApiModelProperty(value = "GeoJSON Properties object", required = true)
    public final PortAreaProperties properties;

    @ApiModelProperty("GeoJSON Geometry object")
    public final Geometry geometry;

    @ApiModelProperty(allowableValues = "Feature", required = true)
    public final String type = "Feature";

    public PortAreaFeature(final String locode, final String portAreaCode,
                           final PortAreaProperties properties,
                           final Point geometry) {
        this.locode = locode;
        this.portAreaCode = portAreaCode;
        this.properties = properties;
        this.geometry = geometry;
    }

}
