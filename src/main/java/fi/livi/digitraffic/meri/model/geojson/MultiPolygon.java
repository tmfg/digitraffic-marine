package fi.livi.digitraffic.meri.model.geojson;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GeoJSON MultiPolygon Geometry object")
public class MultiPolygon extends Geometry<List<List<List<List<Double>>>>> {

    public MultiPolygon() {
        super(GeometryType.MultiPolygon);
    }

    @Schema(required = true, allowableValues = "MultiPolygon", example = "MultiPolygon")
    @Override
    public String getType() {
        return super.getType();
    }

    @Schema(required = true, example = "[ [26.97677492, 65.34673850], [26.98433065, 65.35836767] ]",
                      description = "An array of Polygon coordinates. " + COORD_FORMAT_WGS84_LONG_INC_ALT, type = "List")
    @Override
    public List<List<List<List<Double>>>> getCoordinates() {
        return super.getCoordinates();
    }
}