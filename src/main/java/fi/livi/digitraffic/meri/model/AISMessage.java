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
        public final int navStat;
        public final int rot;
        public final int posAcc;

        public final int heading;
        public final long timestamp;
        public final int raim;

        public final double speed;
        public final double course;
        public final int utcSec;


        @JsonCreator
        public AISAttributes(@JsonProperty("mmsi") final int mmsi,
                             @JsonProperty("timestamp") final long timestamp,
                             @JsonProperty("speed") final double speed,
                             @JsonProperty("course") final double course,
                             @JsonProperty("utc_sec") final int utcSec,
                             @JsonProperty("nav_stat") final int navStat,
                             @JsonProperty("rot") final int rot,
                             @JsonProperty("pos_acc") final int posAcc,
                             @JsonProperty("heading") final int heading,
                             @JsonProperty("raim") final int raim) {
            this.mmsi = mmsi;
            this.timestamp = timestamp;
            this.speed = speed;
            this.course = course;
            this.utcSec = utcSec;
            this.navStat = navStat;
            this.rot = rot;
            this.posAcc = posAcc;
            this.heading = heading;
            this.raim = raim;
        }
    }
}
