package fi.livi.digitraffic.meri.model.geojson;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "GeoJSON MultiLineString Geometry object")
public class MultiLineString extends Geometry<List<List<List<Double>>>> {

    public MultiLineString() {
        super(GeometryType.MultiLineString);
    }

    @ApiModelProperty(required = true, allowableValues = "MultiLineString", example = "MultiLineString")
    @Override
    public String getType() {
        return super.getType();
    }

    @ApiModelProperty(required = true, position = 2, example = "\"[ [ [100.0, 0.0], [101.0, 1.0] ], [ [102.0, 2.0], [103.0, 3.0] ] ]",
                      value = "An array of LineString coordinates. " + COORD_FORMAT_WGS84_LONG_INC_ALT, dataType = "List")
    @Override
    public List<List<List<Double>>> getCoordinates() {
        return super.getCoordinates();
    }
}