package fi.livi.digitraffic.meri.model.geojson;

import java.util.List;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "GeoJSON LineString Geometry object", parent = MultiPoint.class)
public class LineString extends MultiPoint {

    public LineString() {
        setType("LineString");
    }

    public void setCoordinates(List<List<Double>> coordinates) {
        super.setCoordinates(coordinates);
    }
}