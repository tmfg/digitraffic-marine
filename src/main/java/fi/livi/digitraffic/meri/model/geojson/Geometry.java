
package fi.livi.digitraffic.meri.model.geojson;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
    "type",
    "coordinates"
})
@Schema(description = "GeoJSON Geometry object")
public class Geometry<T extends List<?>> extends GeoJsonObject {

    public static final String COORD_FORMAT_WGS84 = "Coordinates are in WGS84 format in decimal degrees.";
    public static final String COORD_FORMAT_WGS84_LONG = "Coordinates are in WGS84 format in decimal degrees: [LONGITUDE, LATITUDE, {ALTITUDE}].";
    public static final String COORD_FORMAT_WGS84_LONG_INC_ALT = COORD_FORMAT_WGS84_LONG + " Altitude is optional and measured in meters.";

    private GeometryType type;

    @Schema(description = COORD_FORMAT_WGS84_LONG_INC_ALT, required = true)
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

    @Schema(description = "Type of GeoJSON Geometry object", allowableValues = "Point,MultiPoint,LineString,MultiLineString,Polygon,MultiPolygon", required = true)
    @Override
    public String getType() {
        return type.name();
    }

    @Override
    public String toString() {
        return "Geometry { type=" + getType() + ", coordinates=" + coordinates + " }";
    }

    @Schema
    public enum GeometryType {
        Point, LineString, Polygon, MultiPoint, MultiLineString, MultiPolygon
    }
}
