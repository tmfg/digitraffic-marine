package fi.livi.digitraffic.meri.model.pooki;

import java.util.List;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "GeoJSON LineString Geometry object", parent = MultiPoint.class)
public class LineString extends MultiPoint {

    public LineString() {}

    public void setCoordinates(List<List<Double>> coordinates) {
        super.setCoordinates(coordinates);
    }

    @Override
    public String toString() {
        return "LineString{} " + super.toString();
    }
}