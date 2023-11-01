package fi.livi.digitraffic.meri.dto.ais.v1;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.dto.geojson.Feature;
import fi.livi.digitraffic.meri.dto.geojson.Point;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GeoJSON Feature object")
@JsonPropertyOrder({
        "mmsi",
        "type",
        "geometry",
        "properties"
})
public class VesselLocationFeatureV1 extends Feature<Point, VesselLocationPropertiesV1> {

    @Schema(description = "Maritime Mobile Service Identity (nine digit identifier)", requiredMode = Schema.RequiredMode.REQUIRED)
    public final int mmsi;

    public VesselLocationFeatureV1(final int mmsi, final VesselLocationPropertiesV1 properties, final Point geometry) {
        super(geometry, properties);
        this.mmsi = mmsi;
    }
}
