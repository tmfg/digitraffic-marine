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

    public void setCoordinates(List<List<List<Double>>> coordinates) {
        super.setCoordinates(coordinates);
    }
}