package fi.livi.digitraffic.meri.dto.ais.external;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fi.livi.digitraffic.meri.model.Validatable;

public class AISMessage implements Validatable {
    public final Geometry geometry;
    public final AISAttributes attributes;

    @JsonCreator
    public AISMessage(@JsonProperty("geometry") final Geometry geometry, @JsonProperty("attributes") final AISAttributes attributes) {
        this.geometry = geometry;
        this.attributes = attributes;
    }

    @Override
    public boolean validate() {
        return geometry != null && attributes.mmsi > 0 && attributes.mmsi < 1000000000;
    }

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

        public static class SpatialReference {
            public final int wkid;

            @JsonCreator
            public SpatialReference(@JsonProperty("wkid") final int wkid) {
                this.wkid = wkid;
            }

            @Override
            public String toString() {
                return "SpatialReference{" +
                        "wkid=" + wkid +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "AISMessage{" +
                "geometry=" + geometry +
                ", attributes=" + attributes +
                '}';
    }

    public static class AISAttributes {
        public final int mmsi;
        public final int navStat;
        public final int rot;
        public final int posAcc;

        public final Integer heading;
        public final long timestamp;
        public final int raim;

        public final double sog;
        public final double cog;

        public final long timestampExternal;

        @JsonCreator
        public AISAttributes(@JsonProperty("mmsi") final int mmsi,
                             @JsonProperty("timestamp_ext") final long timestampExternal,
                             @JsonProperty("SOG") final double speed,
                             @JsonProperty("COG") final double course,
                             @JsonProperty("nav_stat") final int navStat,
                             @JsonProperty("rot") final int rot,
                             @JsonProperty("pos_acc") final int posAcc,
                             @JsonProperty("heading") final Integer heading,
                             @JsonProperty("raim") final int raim,
                             @JsonProperty("timestamp") final long timestamp) {
            this.mmsi = mmsi;
            this.timestampExternal = timestampExternal;
            this.sog = speed;
            this.cog = course;
            this.navStat = navStat;
            this.rot = rot;
            this.posAcc = posAcc;
            this.heading = heading;
            this.raim = raim;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "AISAttributes{" +
                    "mmsi=" + mmsi +
                    ", navStat=" + navStat +
                    ", rot=" + rot +
                    ", posAcc=" + posAcc +
                    ", heading=" + heading +
                    ", timestamp=" + timestamp +
                    ", raim=" + raim +
                    ", sog=" + sog +
                    ", cog=" + cog +
                    ", timestampExternal=" + timestampExternal +
                    '}';
        }
    }
}
