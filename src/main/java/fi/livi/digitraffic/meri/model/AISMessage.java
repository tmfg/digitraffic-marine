package fi.livi.digitraffic.meri.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class AISMessage {
    public final Geometry geometry;
    public final AISAttributes attributes;

    @JsonCreator
    public AISMessage(@JsonProperty("geometry") final Geometry geometry, @JsonProperty("attributes") final AISAttributes attributes) {
        this.geometry = geometry;
        this.attributes = attributes;
    }

    @Immutable
    public static class Geometry {
        public final double x;
        public final double y;
        public final SpatialReference spatialReference;

        @JsonCreator
        public Geometry(@JsonProperty("x") final double x, @JsonProperty("y") final double y, @JsonProperty("spatialReference") final
        SpatialReference spatialReference) {
            this.x = x;
            this.y = y;
            this.spatialReference = spatialReference;
        }

        @Immutable
        public static class SpatialReference {
            public final int wkid;

            @JsonCreator
            public SpatialReference(@JsonProperty("wkid") final int wkid) {
                this.wkid = wkid;
            }
        }
    }

    @Immutable
    public static class AISAttributes {
        public final int mmsi;
        public final long timestamp;
        public final double speed;
        public final double course;
        public final int utc_sec;

        @JsonCreator
        public AISAttributes(@JsonProperty("mmsi") final int mmsi,
                @JsonProperty("timestamp") final long timestamp, @JsonProperty("speed") final double speed,
                @JsonProperty("course") final double course, @JsonProperty("utc_sec") final int utc_sec) {
            this.mmsi = mmsi;
            this.timestamp = timestamp;
            this.speed = speed;
            this.course = course;
            this.utc_sec = utc_sec;
        }
    }
}