package fi.livi.digitraffic.meri.model.portnet.metadata;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({
        "locode",
        "type",
        "geometry",
        "properties"
})
@ApiModel(description = "GeoJSON Feature object")
public class BerthFeature {
    @ApiModelProperty(value = "Maritime Mobile Service Identity", required = true)
    public final String locode;

    @ApiModelProperty(value = "Port area code", required = true)
    public final String portAreaCode;

    @ApiModelProperty(value = "Berth code", required = true)
    public final String berthCode;

    @ApiModelProperty(value = "GeoJSON Properties object", required = true)
    public final BerthProperties properties;

    @ApiModelProperty(allowableValues = "Feature", required = true)
    public final String type = "Feature";

    public BerthFeature(final String locode, final String portAreaCode, final String berthCode, final BerthProperties properties) {
        this.locode = locode;
        this.portAreaCode = portAreaCode;
        this.berthCode = berthCode;
        this.properties = properties;
    }
}
