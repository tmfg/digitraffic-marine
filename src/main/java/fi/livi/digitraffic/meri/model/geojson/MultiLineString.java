package fi.livi.digitraffic.meri.model.geojson;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "GeoJSON MultiPoint Geometry object", parent = Geometry.class, subTypes = {LineString.class})
@JsonSubTypes({ @JsonSubTypes.Type(LineString.class)})
public class MultiLineString extends Geometry<List<List<List<Double>>>> {

    public MultiLineString() {};

    public void setCoordinates(List<List<List<Double>>> coordinates) {
        super.setCoordinates(coordinates);
    }

    @Override
    public String toString() {
        return "MultiPoint{} " + super.toString();
    }

}