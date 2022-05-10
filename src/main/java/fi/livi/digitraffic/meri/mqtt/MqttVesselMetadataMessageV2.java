package fi.livi.digitraffic.meri.mqtt;

import com.fasterxml.jackson.annotation.JsonInclude;
import fi.livi.digitraffic.meri.model.ais.VesselMessage;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MqttVesselMetadataMessageV2 {
    public final long timestamp;

    public final String destination;
    public final String name;
    public final int draught;
    public final long eta;
    public final int posType;
    public final long refA, refB, refC, refD;
    public final String callSign;
    public final long imo;
    public final int type;

    public MqttVesselMetadataMessageV2(final VesselMessage.VesselAttributes vesselAttributes) {
        this.timestamp = vesselAttributes.timestamp;
        this.destination = vesselAttributes.dest;
        this.name = vesselAttributes.vesselName;
        this.draught = vesselAttributes.draught;
        this.eta = vesselAttributes.eta;
        this.posType = vesselAttributes.posType;
        this.refA = vesselAttributes.referencePointA;
        this.refB = vesselAttributes.referencePointB;
        this.refC = vesselAttributes.referencePointC;
        this.refD = vesselAttributes.referencePointD;
        this.callSign = vesselAttributes.callSign;
        this.imo = vesselAttributes.imo;
        this.type = vesselAttributes.shipAndCargoType;
    }
}
