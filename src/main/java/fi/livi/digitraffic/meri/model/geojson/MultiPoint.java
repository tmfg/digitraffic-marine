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

    public void setCoordinates(List<List<Double>> coordinates) {
        super.setCoordinates(coordinates);
    }
}