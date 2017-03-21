
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
@ApiModel(description = "GeoJSON Geometry object", parent = GeoJsonObject.class, subTypes = { LineString.class, MultiLineString.class, MultiPoint.class, MultiPolygon.class, Point.class, Polygon.class})
public class Geometry<T extends List<?>> extends GeoJsonObject {

    @ApiModelProperty(value = "WGS84 coordinates in decimal degrees. [LONGITUDE, LATITUDE, ALTITUDE]", required = true)
    @JsonProperty(required = true)
    private T coordinates;

    public Geometry() {}

    public Geometry(final T coordinates) {
        this.coordinates = coordinates;
    }

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
        return "Geometry { type=" + getType() + ", coordinates=" + coordinates + " }";
    }

}
