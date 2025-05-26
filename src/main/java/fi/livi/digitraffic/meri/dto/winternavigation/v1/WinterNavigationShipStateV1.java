package fi.livi.digitraffic.meri.dto.winternavigation.v1;

import java.time.Instant;

import fi.livi.digitraffic.meri.model.winternavigation.PositionAccuracy;
import fi.livi.digitraffic.meri.model.winternavigation.PositionSource;
import io.swagger.v3.oas.annotations.media.Schema;

public class WinterNavigationShipStateV1 {

    @Schema(description = "Timestamp of position observation")
    public final Instant timestamp;

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
    public final Instant movingSince;

    @Schema(description = "When the ship was observed to having been stopped")
    public final Instant stoppedSince;

    @Schema(description = "When the ship was no more observed / was deactivated from the system")
    public final Instant inactiveSince;

    public WinterNavigationShipStateV1(final Instant timestamp, final String posPrintable, final PositionAccuracy posAccuracy,
                                       final PositionSource posSource, final String posArea, final Double speed, final Double course, final Double heading, final Double aisDraught,
                                       final Integer aisState, final String aisStateText, final String aisDestination, final Instant movingSince, final Instant stoppedSince,
                                       final Instant inactiveSince) {
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
