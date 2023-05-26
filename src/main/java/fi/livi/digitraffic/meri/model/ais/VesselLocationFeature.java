package fi.livi.digitraffic.meri.model.ais;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Feature;
import fi.livi.digitraffic.meri.model.geojson.Point;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GeoJSON Feature object")
@JsonPropertyOrder({
        "mmsi",
        "type",
        "geometry",
        "properties"
})
public class VesselLocationFeature extends Feature<Point, VesselLocationProperties> {

    @Schema(description = "Maritime Mobile Service Identity (nine digit identifier)", requiredMode = Schema.RequiredMode.REQUIRED)
    public final int mmsi;

    public VesselLocationFeature(final int mmsi, final VesselLocationProperties properties, final Point geometry) {
        super(geometry, properties);
        this.mmsi = mmsi;
    }
}
