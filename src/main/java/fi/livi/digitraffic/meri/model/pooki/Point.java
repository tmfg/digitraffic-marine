package fi.livi.digitraffic.meri.model.pooki;

import java.util.List;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "GeoJSON Point Geometry object", parent = Geometry.class)
public class Point extends Geometry<List<Double>> {

    private List<Double> coordinates;

    public Point() {}

    @Override
    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "Point{" + "coordinates=" + coordinates + "} " + super.toString();
    }
}