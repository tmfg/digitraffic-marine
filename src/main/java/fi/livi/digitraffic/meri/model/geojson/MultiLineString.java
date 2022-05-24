package fi.livi.digitraffic.meri.model.geojson;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "GeoJSON MultiLineString Geometry object")
public class MultiLineString extends Geometry<List<List<List<Double>>>> {

    public MultiLineString() {
        super(GeometryType.MultiLineString);
    }

    @Schema(required = true, allowableValues = "MultiLineString", example = "MultiLineString")
    @Override
    public String getType() {
        return super.getType();
    }

    @Schema(required = true, example = "\"[ [ [100.00000000, 0.00000000], [101.00000000, 1.00000000] ], [ [102.00000000, 2.00000000], [103.00000000, 3.00000000] ] ]",
                      description = "An array of LineString coordinates. " + COORD_FORMAT_WGS84_LONG_INC_ALT, type = "List")
    @Override
    public List<List<List<Double>>> getCoordinates() {
        return super.getCoordinates();
    }
}