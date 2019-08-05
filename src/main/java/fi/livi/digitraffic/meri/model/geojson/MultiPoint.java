package fi.livi.digitraffic.meri.model.geojson;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "GeoJSON MultiPoint Geometry object")
public class MultiPoint extends Geometry<List<List<Double>>> {

    public MultiPoint() {
        super(GeometryType.MultiPoint);
    }

    @ApiModelProperty(required = true, allowableValues = "MultiPoint", example = "MultiPoint")
    @Override
    public String getType() {
        return super.getType();
    }

    @ApiModelProperty(required = true, position = 2, example = "[ [26.97677492, 65.34673850], [26.98433065, 65.35836767] ]",
                      value = "An array of Point coordinates. " + COORD_FORMAT_WGS84_LONG_INC_ALT, dataType = "List")
    @Override
    public List<List<Double>> getCoordinates() {
        return super.getCoordinates();
    }
}