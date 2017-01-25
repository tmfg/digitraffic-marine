package fi.livi.digitraffic.meri.model.geojson;

import java.util.List;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "GeoJSON Polygon Geometry object", parent = Geometry.class)
public class Polygon extends Geometry<List<List<List<Double>>>> {

    public Polygon() {
        setType("Polygon");
    }

    public void setCoordinates(List<List<List<Double>>> coordinates) {
        super.setCoordinates(coordinates);
    }
}