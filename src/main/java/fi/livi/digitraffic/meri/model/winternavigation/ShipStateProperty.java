package fi.livi.digitraffic.meri.model.winternavigation;

import java.time.ZonedDateTime;

import fi.livi.digitraffic.meri.service.winternavigation.dto.PositionAccuracy;
import fi.livi.digitraffic.meri.service.winternavigation.dto.PositionSource;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ShipState")
public class ShipStateProperty {

    @Schema(description = "Timestamp of position observation")
    public final ZonedDateTime timestamp;

    @Schema(description = "Pre-formatted friendly output")
    public final String posPrintable;

    @Schema(description = "Ship position accuracy")
    public final PositionAccuracy posAccuracy;

    @Schema(description = "Ship position source")
    public final PositionSource posSource;

    public final String posArea;

    @Schema(description = "Observed speed in knots")
    public final Double speed;

    @Schema(description = "Observed course in degrees")
    public final Double course;

    @Schema(description = "Observed heading in degrees")
    public final Double heading;

    @Schema(description = "Ship's current dynamic draught (entered into its own AIS device)")
    public final Double aisDraught;

    @Schema(description = "0-15, see AIS standard")
    public final Integer aisState;

    @Schema(description = "Text description of AIS state")
    public final String aisStateText;

    @Schema(description = "Destination text entered by ship into its own AIS transponder; often significantly different " +
                              "from what is available in “Voyage” section")
    public final String aisDestination;

    @Schema(description = "When the ship was observed to start moving")
    public final ZonedDateTime movingSince;

    @Schema(description = "When the ship was observed to having been stopped")
    public final ZonedDateTime stoppedSince;

    @Schema(description = "When the ship was no more observed / was deactivated from the system")
    public final ZonedDateTime inactiveSince;

    public ShipStateProperty(ZonedDateTime timestamp, String posPrintable, PositionAccuracy posAccuracy,
                             PositionSource posSource, String posArea, Double speed, Double course, Double heading, Double aisDraught,
                             Integer aisState, String aisStateText, String aisDestination, ZonedDateTime movingSince, ZonedDateTime stoppedSince,
                             ZonedDateTime inactiveSince) {
        this.timestamp = timestamp;
        this.posPrintable = posPrintable;
        this.posAccuracy = posAccuracy;
        this.posSource = posSource;
        this.posArea = posArea;
        this.speed = speed;
        this.course = course;
        this.heading = heading;
        this.aisDraught = aisDraught;
        this.aisState = aisState;
        this.aisStateText = aisStateText;
        this.aisDestination = aisDestination;
        this.movingSince = movingSince;
        this.stoppedSince = stoppedSince;
        this.inactiveSince = inactiveSince;
    }
}
