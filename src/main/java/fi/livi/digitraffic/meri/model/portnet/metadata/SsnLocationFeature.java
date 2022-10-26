package fi.livi.digitraffic.meri.model.portnet.metadata;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Feature;
import fi.livi.digitraffic.meri.model.geojson.Point;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
        "locode",
        "type",
        "geometry",
        "properties"
})
//@Schema(description = "") // TODO
public class SsnLocationFeature extends Feature<Point, SsnLocationProperties> {

    @Schema(description = "Maritime Mobile Service Identity", required = true)
    public final String locode;

    public SsnLocationFeature(final String locode, final SsnLocationProperties properties, final Point geometry) {
        super(geometry, properties);
        this.locode = locode;
    }

}
