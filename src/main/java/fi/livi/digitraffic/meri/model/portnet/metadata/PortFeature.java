package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.dto.LastModifiedSupport;
import fi.livi.digitraffic.meri.model.geojson.Feature;
import fi.livi.digitraffic.meri.model.geojson.Point;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
        "locode",
        "type",
        "geometry",
        "properties"
})
@Schema(description = "Port GeoJSON feature")
public class PortFeature extends Feature<Point, PortProperties> implements LastModifiedSupport {

    @Schema(description = "Maritime Mobile Service Identity", required = true)
    public final String locode;

    public PortFeature(final String locode, final PortProperties properties, final Point geometry) {
        super(geometry, properties);
        this.locode = locode;
    }

    @Override
    public Instant getLastModified() {
        return getProperties().getLastModified();
    }
}
