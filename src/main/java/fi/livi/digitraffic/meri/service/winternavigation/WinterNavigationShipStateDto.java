package fi.livi.digitraffic.meri.service.winternavigation;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WinterNavigationShipStateDto {

    public final ZonedDateTime timestamp;

    public final Double longitude;

    public final Double latitude;

    public final String posPrintable;

    // FIXME: Enum
    public final Integer posAccuracy;

    // FIXME: Enum
    public final String posSource;

    public final String posArea;

    public final Double speed;

    public final Double course;

    public final Double heading;

    public final Double aisDraught;

    public final Integer aisState;

    public final String aisStateText;

    public final String aisDestination;

    public final ZonedDateTime movingSince;

    public final ZonedDateTime stoppedSince;

    public final ZonedDateTime inactiveSince;

    public WinterNavigationShipStateDto(@JsonProperty("timestamp") final ZonedDateTime timestamp,
                                        @JsonProperty("lon") final Double longitude,
                                        @JsonProperty("lat") final Double latitude,
                                        @JsonProperty("pos_printable") final String posPrintable,
                                        @JsonProperty("pos_accuracy") final Integer posAccuracy,
                                        @JsonProperty("pos_source") final String posSource,
                                        @JsonProperty("pos_area") final String posArea,
                                        @JsonProperty("speed") final Double speed,
                                        @JsonProperty("course") final Double course,
                                        @JsonProperty("heading") final Double heading,
                                        @JsonProperty("ais_draught") final Double aisDraught,
                                        @JsonProperty("ais_state") final Integer aisState,
                                        @JsonProperty("ais_state_text") final String aisStateText,
                                        @JsonProperty("ais_destination") final String aisDestination,
                                        @JsonProperty("moving_since") final ZonedDateTime movingSince,
                                        @JsonProperty("stopped_since") final ZonedDateTime stoppedSince,
                                        @JsonProperty("inactive_since") final ZonedDateTime inactiveSince) {
        this.timestamp = timestamp;
        this.longitude = longitude;
        this.latitude = latitude;
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
