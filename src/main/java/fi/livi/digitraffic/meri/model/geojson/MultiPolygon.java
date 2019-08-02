package fi.livi.digitraffic.meri.model.geojson;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "GeoJSON MultiPolygon Geometry object", parent = Geometry.class)
public class MultiPolygon extends Geometry<List<List<List<List<Double>>>>> {

    public MultiPolygon() {
        super(GeometryType.MultiPolygon);
    }

    @ApiModelProperty(required = true, allowableValues = "MultiPolygon", example = "MultiPolygon")
    @Override
    public String getType() {
        return super.getType();
    }
}