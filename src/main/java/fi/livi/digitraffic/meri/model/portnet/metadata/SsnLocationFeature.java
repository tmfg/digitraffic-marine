package fi.livi.digitraffic.meri.model.portnet.metadata;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Feature;
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
public class SsnLocationFeature extends Feature<Point, SsnLocationProperties> {

    @ApiModelProperty(value = "Maritime Mobile Service Identity", required = true)
    public final String locode;

    public SsnLocationFeature(final String locode, final SsnLocationProperties properties, final Point geometry) {
        super(geometry, properties);
        this.locode = locode;
    }

}
