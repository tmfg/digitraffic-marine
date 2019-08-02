package fi.livi.digitraffic.meri.model.portnet.metadata;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Feature;
import fi.livi.digitraffic.meri.model.geojson.Point;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({
        "locode",
        "type",
        "geometry",
        "properties"
})
public class BerthFeature extends Feature<Point, BerthProperties> {

    @ApiModelProperty(value = "Maritime Mobile Service Identity", required = true)
    public final String locode;

    @ApiModelProperty(value = "Port area code", required = true)
    public final String portAreaCode;

    @ApiModelProperty(value = "Berth code", required = true)
    public final String berthCode;

    public BerthFeature(final String locode, final String portAreaCode, final String berthCode, final BerthProperties properties) {
        super(null, properties);
        this.locode = locode;
        this.portAreaCode = portAreaCode;
        this.berthCode = berthCode;
    }
}
