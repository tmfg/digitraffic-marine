package fi.livi.digitraffic.meri.model.geojson;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "GeoJSON MultiPoint Geometry object", parent = Geometry.class, subTypes = {LineString.class})
@JsonSubTypes({ @JsonSubTypes.Type(LineString.class)})
public class MultiPolygon extends Geometry<List<List<List<List<Double>>>>> {

    public MultiPolygon() {
        setType("MultiPolygon");
    };

    public void setCoordinates(List<List<List<List<Double>>>> coordinates) {
        super.setCoordinates(coordinates);
    }
}