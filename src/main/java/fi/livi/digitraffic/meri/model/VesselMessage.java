package fi.livi.digitraffic.meri.model;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Immutable
public class VesselMessage {
    public final VesselAttributes vesselAttributes;

    @JsonCreator
    public VesselMessage(@JsonProperty("attributes") final VesselAttributes vesselAttributes) {
        this.vesselAttributes = vesselAttributes;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VesselAttributes {
        public final int mmsi;
        public final int imo;
        public final long timestamp;
        public final String callSign;
        public final String vesselName;
        public final int shipAndCargoType;
        public final int posType;
        public final long eta;
        public final int draught;
        public final String dest;

        public VesselAttributes(@JsonProperty("mmsi") final int mmsi,
                                @JsonProperty("imo") final int imo,
                                @JsonProperty("timestamp_ext") final long timestampExt,
                                @JsonProperty("call_sign") final String callSign,
                                @JsonProperty("vessel_name") final String vesselName,
                                @JsonProperty("ship_and_cargo_type") final int shipAndCargoType,
                                @JsonProperty("pos_type") final int posType,
                                @JsonProperty("eta") final int eta,
                                @JsonProperty("draugh") final int draught,
                                @JsonProperty("dest") final String dest) {
            this.mmsi = mmsi;
            this.imo = imo;
            this.timestamp = timestampExt;
            this.callSign = callSign;
            this.vesselName = vesselName;
            this.shipAndCargoType = shipAndCargoType;
            this.posType = posType;
            this.eta = eta;
            this.draught = draught;
            this.dest = dest;
        }
    }
}
