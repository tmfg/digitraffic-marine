package fi.livi.digitraffic.meri.model.winternavigation;

import java.time.ZonedDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ShipVoyage")
public class ShipVoyageProperty {

    @Schema(description = "UN locode of the previous port, if known")
    public final String fromLocode;

    @Schema(description = "Port name, if known")
    public final String fromName;

    @Schema(description = "Actual Time of Departure, if known")
    public final ZonedDateTime fromAtd;

    @Schema(description = "UN locode of the current port, if the ship is in harbour")
    public final String inLocode;

    @Schema(description = "Port name, if applicable")
    public final String inName;

    @Schema(description = "Actual Time of Arrival to current port, if known")
    public final ZonedDateTime inAta;

    @Schema(description = "Estimated Time of Departure to next destination, if known; note that may be confidential")
    public final ZonedDateTime inEtd;

    @Schema(description = "UN locode of the destination port, if known")
    public final String destLocode;

    @Schema(description = "Destination port name, if known")
    public final String destName;

    @Schema(description = "Estimated Time of Arrival to destination, if known; note that may be confidential")
    public final ZonedDateTime destEta;

    public ShipVoyageProperty(String fromLocode, String fromName, ZonedDateTime fromAtd, String inLocode, String inName, ZonedDateTime inAta, ZonedDateTime inEtd,
                              String destLocode, String destName, ZonedDateTime destEta) {
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
