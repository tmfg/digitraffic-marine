package fi.livi.digitraffic.meri.dto.winternavigation.v1;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

public class WinterNavigationShipVoyageV1 {

    @Schema(description = "UN locode of the previous port, if known")
    public final String fromLocode;

    @Schema(description = "Port name, if known")
    public final String fromName;

    @Schema(description = "Actual Time of Departure, if known")
    public final Instant fromAtd;

    @Schema(description = "UN locode of the current port, if the ship is in harbour")
    public final String inLocode;

    @Schema(description = "Port name, if applicable")
    public final String inName;

    @Schema(description = "Actual Time of Arrival to current port, if known")
    public final Instant inAta;

    @Schema(description = "Estimated Time of Departure to next destination, if known; note that may be confidential")
    public final Instant inEtd;

    @Schema(description = "UN locode of the destination port, if known")
    public final String destLocode;

    @Schema(description = "Destination port name, if known")
    public final String destName;

    @Schema(description = "Estimated Time of Arrival to destination, if known; note that may be confidential")
    public final Instant destEta;

    public WinterNavigationShipVoyageV1(final String fromLocode, final String fromName, final Instant fromAtd, final String inLocode, final String inName, final Instant inAta, final Instant inEtd,
                                        final String destLocode, final String destName, final Instant destEta) {
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
