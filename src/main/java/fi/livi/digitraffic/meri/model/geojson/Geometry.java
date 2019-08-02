
package fi.livi.digitraffic.meri.model.geojson;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({
    "type",
    "coordinates"
})
@ApiModel(description = "GeoJSON Geometry object")
public class Geometry<T extends List<?>> extends GeoJsonObject {

    private GeometryType type;

    @ApiModelProperty(value = "WGS84 coordinates in decimal degrees. [LONGITUDE, LATITUDE, {ALTITUDE}]. Altitude is optional.", required = true)
    @JsonProperty(required = true)
    private T coordinates;

    public Geometry() {
    }

    public Geometry(final GeometryType type) {
        this.type = type;
    }

    public Geometry(final GeometryType type, final T coordinates) {
        this(type);
        this.coordinates = coordinates;
    }

    public T getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(T coordinates) {
        this.coordinates = coordinates;
    }

    @ApiModelProperty( value = "Type of GeoJSON Geometry object", allowableValues = "Point,MultiPoint,LineString,MultiLineString,Polygon,MultiPolygon", required = true)
    @Override
    public String getType() {
        return type.name();
    }

    @Override
    public String toString() {
        return "Geometry { type=" + getType() + ", coordinates=" + coordinates + " }";
    }

    @ApiModel
    public enum GeometryType {
        Point, LineString, Polygon, MultiPoint, MultiLineString, MultiPolygon
    }
}
