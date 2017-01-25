package fi.livi.digitraffic.meri.model.geojson;

import java.util.Arrays;
import java.util.List;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "GeoJSON Point Geometry object", parent = Geometry.class)
public class Point extends Geometry<List<Double>> {
    private List<Double> coordinates;

    public Point(final double x, final double y) {
        super(Arrays.asList(x, y));
        setType("Point");
    }

    @Override
    public void setCoordinates(final List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
