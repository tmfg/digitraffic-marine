
package fi.livi.digitraffic.meri.model.geojson;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({
    "type",
    "coordinates"
})
@JsonSubTypes({ @JsonSubTypes.Type(Point.class), @JsonSubTypes.Type(Polygon.class), @JsonSubTypes.Type(MultiPoint.class)})
@ApiModel(description = "GeoJSON Geometry object", parent = GeoJsonObject.class, subTypes = { Point.class, LineString.class, MultiPoint.class, Polygon.class})
public class Geometry<T extends List<?>> extends GeoJsonObject {

    @ApiModelProperty(value = "WGS84 coordinates in decimal degrees. [LONGITUDE, LATITUDE, ALTITUDE]", required = true)
    @JsonProperty(required = true)
    private T coordinates;

    public Geometry() {}

    public T getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(T coordinates) {
        this.coordinates = coordinates;
    }

    @ApiModelProperty(allowableValues = "Point,MultiPoint,LineString,MultiLineString,Polygon,MultiPolygon", required = true)
    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public String toString() {
        return "Geometry{" + "coordinates=" + coordinates + "} " + super.toString();
    }

}
