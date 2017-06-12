package fi.livi.digitraffic.meri.model.winternavigation;

import java.sql.Timestamp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ShipVoyage")
public class ShipVoyageProperty {

    @ApiModelProperty(value = "UN locode of the previous port, if known")
    public final String fromLocode;

    @ApiModelProperty(value = "Port name, if known")
    public final String fromName;

    @ApiModelProperty(value = "Actual Time of Departure, if known")
    public final Timestamp fromAtd;

    @ApiModelProperty(value = "UN locode of the current port, if the ship is in harbour")
    public final String inLocode;

    @ApiModelProperty(value = "Port name, if applicable")
    public final String inName;

    @ApiModelProperty(value = "Actual Time of Arrival to current port, if known")
    public final Timestamp inAta;

    @ApiModelProperty(value = "Estimated Time of Departure to next destination, if known; note that may be confidential")
    public final Timestamp inEtd;

    @ApiModelProperty(value = "UN locode of the destination port, if known")
    public final String destLocode;

    @ApiModelProperty(value = "Destination port name, if known")
    public final String destName;

    @ApiModelProperty(value = "Estimated Time of Arrival to destination, if known; note that may be confidential")
    public final Timestamp destEta;

    public ShipVoyageProperty(String fromLocode, String fromName, Timestamp fromAtd, String inLocode, String inName, Timestamp inAta, Timestamp inEtd,
                              String destLocode, String destName, Timestamp destEta) {
        this.fromLocode = fromLocode;
        this.fromName = fromName;
        this.fromAtd = fromAtd;
        this.inLocode = inLocode;
        this.inName = inName;
        this.inAta = inAta;
        this.inEtd = inEtd;
        this.destLocode = destLocode;
        this.destName = destName;
        this.destEta = destEta;
    }
}
