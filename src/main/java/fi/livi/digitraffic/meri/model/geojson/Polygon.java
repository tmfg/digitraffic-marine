package fi.livi.digitraffic.meri.model.geojson;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "GeoJSON Polygon Geometry object")
public class Polygon extends Geometry<List<List<List<Double>>>> {

    public Polygon() {
        super(GeometryType.Polygon);
    }

    @ApiModelProperty(required = true, allowableValues = "Polygon", example = "Polygon")
    @Override
    public String getType() {
        return super.getType();
    }

    @ApiModelProperty(required = true, position = 2,
                      example = "[ [ [100.00000000, 0.00000000], [101.00000000, 0.00000000], [101.00000000, 1.00000000], [100.00000000, 1.00000000], [100.00000000, 0.00000000] ], " +
                                  "[ [100.20000000, 0.20000000], [100.80000000, 0.20000000], [100.80000000, 0.80000000], [100.20000000, 0.80000000], [100.20000000, 0.20000000] ] ]",
                      value = "An array of LinearRing coordinates (LineString coordinates where the first and last points are equivalent). " +
                              "The first element in the array represents the exterior ring. Any subsequent elements represent interior rings (or holes).. " +
                              COORD_FORMAT_WGS84_LONG_INC_ALT)
    @Override
    public List<List<List<Double>>> getCoordinates() {
        return super.getCoordinates();
    }
}