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
public class PortAreaFeature extends Feature<Point, PortAreaProperties> {

    @Schema(description = "Maritime Mobile Service Identity", required = true)
    public final String locode;

    @Schema(description = "Port area code", required = true)
    public final String portAreaCode;

    public PortAreaFeature(final String locode, final String portAreaCode,
                           final PortAreaProperties properties,
                           final Point geometry) {
        super(geometry, properties);
        this.locode = locode;
        this.portAreaCode = portAreaCode;
    }

}
