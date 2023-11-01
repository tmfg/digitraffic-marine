package fi.livi.digitraffic.meri.dto.portcall.v1.port;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.dto.geojson.Feature;
import fi.livi.digitraffic.meri.dto.geojson.Point;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
        "locode",
        "type",
        "geometry",
        "properties"
})
@Schema(description = "Port area GeoJSON feature")
public class PortAreaFeature extends Feature<Point, PortAreaProperties> {

    @Schema(description = "Maritime Mobile Service Identity", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String locode;

    @Schema(description = "Port area code", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String portAreaCode;

    public PortAreaFeature(final String locode, final String portAreaCode,
                           final PortAreaProperties properties,
                           final Point geometry) {
        super(geometry, properties);
        this.locode = locode;
        this.portAreaCode = portAreaCode;
    }

}
