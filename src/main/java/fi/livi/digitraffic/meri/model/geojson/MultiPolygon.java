package fi.livi.digitraffic.meri.model.geojson;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "GeoJSON MultiPolygon Geometry object")
public class MultiPolygon extends Geometry<List<List<List<List<Double>>>>> {

    public MultiPolygon() {
        super(GeometryType.MultiPolygon);
    }

    @ApiModelProperty(required = true, allowableValues = "MultiPolygon", example = "MultiPolygon")
    @Override
    public String getType() {
        return super.getType();
    }

    @ApiModelProperty(required = true, position = 2, example = "[ [26.976774926733796, 65.34673850731987], [26.984330656240413, 65.35836767060651] ]",
                      value = "An array of Polygon coordinates. " + COORD_FORMAT_WGS84_LONG_INC_ALT, dataType = "List")
    @Override
    public List<List<List<List<Double>>>> getCoordinates() {
        return super.getCoordinates();
    }
}