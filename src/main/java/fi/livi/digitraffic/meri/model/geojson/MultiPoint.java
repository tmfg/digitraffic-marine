package fi.livi.digitraffic.meri.model.geojson;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "GeoJSON MultiPoint Geometry object", parent = Geometry.class, subTypes = {LineString.class})
@JsonSubTypes({ @JsonSubTypes.Type(LineString.class)})
public class MultiPoint extends Geometry<List<List<Double>>> {

    public MultiPoint() {
        setType("MultiPoint");
    };

    public void setCoordinates(List<List<Double>> coordinates) {
        super.setCoordinates(coordinates);
    }
}