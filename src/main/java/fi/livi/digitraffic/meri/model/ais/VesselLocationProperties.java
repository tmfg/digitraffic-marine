package fi.livi.digitraffic.meri.model.ais;

import java.time.Instant;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.v3.oas.annotations.media.Schema;

public class VesselLocationProperties extends Properties {
    @Schema(description = "Maritime Mobile Service Identity (nine digit identifier)", required = true)
    public final int mmsi;

    @Schema(description = "Speed over ground in 1/10 knot steps, 1023 = not available, 1022 = 102.2 knots or higher ", allowableValues = "range[0,1023]", required = true)
    public final double sog;

    @Schema(description = "Course over ground in 1/10 degree steps, 3600 = not available (default)", allowableValues = "range[0,3600]", required = true)
    public final double cog;

    @Schema(description = "Navigational status. \n" +
        "Value range between 0 - 15. \n" +
        "0 = under way using engine, \n1 = at anchor, \n2 = not under command, \n3 = restricted maneuverability, \n4 = constrained by her draught, \n" +
        "5 = moored, \n6 = aground, \n7 = engaged in fishing, \n8 = under way sailing, \n" +
        "9 = reserved for future amendment of navigational status for ships carrying DG, HS, or MP, or IMO hazard or pollutant category C, high speed craft (HSC), \n" +
        "10 = reserved for future amendment of navigational status for ships carrying dangerous goods (DG), harmful substances (HS) or marine pollutants (MP), or IMO hazard or pollutant category A, wing in ground (WIG), \n" +
        "11 = power-driven vessel towing astern (regional use),\n" +
        "12 = power-driven vessel pushing ahead or towing alongside (regional use), \n" +
        "13 = reserved for future use, \n" +
        "14 = AIS-SART (active), MOB-AIS, EPIRB-AIS, \n" +
        "15 = default",
        allowableValues = "range[0,15]", required = true)
    public final int navStat;

    @Schema(description = "Rate of turn, ROT[AIS]. \n" +
        "Values range between -128 - 127. –128 indicates that value is not available (default).\n" +
        "Coded by ROT[AIS] = 4.733 SQRT(ROT[IND]) where ROT[IND] is the Rate of Turn degrees per minute, as indicated by an external sensor. \n" +
        "+127 = turning right at 720 degrees per minute or higher\n" +
        "-127 = turning left at 720 degrees per minute or higher.",
        allowableValues = "range[-128,127]", required = true)
    public final int rot;

    @Schema(description = "Position accuracy, 1 = high, 0 = low", allowableValues = "0, 1", required = true)
    public final boolean posAcc;

    @Schema(description = "Receiver autonomous integrity monitoring (RAIM) flag of electronic position fixing device", allowableValues = "0, 1", required = true)
    public final boolean raim;

    @Schema(description = "Degrees (0-359), 511 = not available (default)", allowableValues = "range[0,359]")
    public final Integer heading;

    @Schema(description = "UTC second when the report was generated by the electronic position system (EPFS). \n" +
        "Values: \n" +
        "0-59 UTC second, \n" +
        "60 if time stamp is not available, which should also be the default value, \n" +
        "61 if positioning system is in manual input mode, \n" +
        "62 if electronic position fixing system operates in estimated (dead reckoning) mode, \n" +
        "63 if the positioning system is inoperative)", allowableValues = "range[0,63]", required = true)
    public final long timestamp;

    @Schema(description = "Location record timestamp in milliseconds from Unix epoch.", required = true)
    public final long timestampExternal;

    public VesselLocationProperties(
        final int mmsi,
        final double sog,
        final double cog,
        final int navStat,
        final int rot,
        final boolean posAcc,
        final boolean raim,
        final Integer heading,
        final long timestamp,
        final long timestampExternal,
        final Instant dataUpdatedTime) {
        super(dataUpdatedTime);
        this.mmsi = mmsi;
        this.sog = sog;
        this.cog = cog;
        this.navStat = navStat;
        this.rot = rot;
        this.posAcc = posAcc;
        this.raim = raim;
        this.heading = heading;
        this.timestamp = timestamp;
        this.timestampExternal = timestampExternal;
    }
}
