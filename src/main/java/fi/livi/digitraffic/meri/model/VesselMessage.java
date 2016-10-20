package fi.livi.digitraffic.meri.model;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VesselMessage implements Validatable {
    public final VesselAttributes vesselAttributes;

    @JsonCreator
    public VesselMessage(@JsonProperty("attributes") final VesselAttributes vesselAttributes) {
        this.vesselAttributes = vesselAttributes;
    }

    @Override
    public boolean validate() {
        return !StringUtils.isEmpty(vesselAttributes.vesselName) && vesselAttributes.mmsi > 0;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VesselAttributes {
        public final int mmsi;
        public final int imo;
        public final long timestamp;
        public final String callSign;
        public final String vesselName;
        public final int shipAndCargoType;
        public final long referencePointA;
        public final long referencePointB;
        public final long referencePointC;
        public final long referencePointD;
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
                                @JsonProperty("ref_point_a") final long referencePointA,
                                @JsonProperty("ref_point_b") final long referencePointB,
                                @JsonProperty("ref_point_c") final long referencePointC,
                                @JsonProperty("ref_point_d") final long referencePointD,
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
            this.referencePointA = referencePointA;
            this.referencePointB = referencePointB;
            this.referencePointC = referencePointC;
            this.referencePointD = referencePointD;
            this.posType = posType;
            this.eta = eta;
            this.draught = draught;
            this.dest = dest;
        }
    }
}
