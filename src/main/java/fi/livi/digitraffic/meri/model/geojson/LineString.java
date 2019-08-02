package fi.livi.digitraffic.meri.model.geojson;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "GeoJSON LineString Geometry object", parent = Geometry.class)
public class LineString extends Geometry<List<List<Double>>> {

    public LineString() {
        super(GeometryType.LineString);
    }

    @ApiModelProperty(required = true, allowableValues = "LineString", example = "LineString")
    @Override
    public String getType() {
        return super.getType();
    }


    public void setCoordinates(List<List<Double>> coordinates) {
        super.setCoordinates(coordinates);
    }
}