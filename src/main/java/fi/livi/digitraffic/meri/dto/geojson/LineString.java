package fi.livi.digitraffic.meri.dto.geojson;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GeoJSON LineString Geometry object")
public class LineString extends Geometry<List<List<Double>>> {

    public LineString() {
        super(GeometryType.LineString);
    }

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = "LineString", example = "LineString")
    @Override
    public String getType() {
        return super.getType();
    }

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "[ [26.97677492, 65.34673850], [26.98433065, 65.35836767] ]",
                      description = "An array of Point coordinates. " + COORD_FORMAT_WGS84_LONG_INC_ALT, type = "List")
    @Override
    public List<List<Double>> getCoordinates() {
        return super.getCoordinates();
    }
}