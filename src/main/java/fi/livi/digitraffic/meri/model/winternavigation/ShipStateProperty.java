package fi.livi.digitraffic.meri.model.winternavigation;

import java.sql.Timestamp;

import fi.livi.digitraffic.meri.service.winternavigation.dto.PositionAccuracy;
import fi.livi.digitraffic.meri.service.winternavigation.dto.PositionSource;

public class ShipStateProperty {

    public final Timestamp timestamp;

    public final String posPrintable;

    public final PositionAccuracy posAccuracy;

    public final PositionSource posSource;

    public final String posArea;

    public final Double speed;

    public final Double course;

    public final Double heading;

    public final Double aisDraught;

    public final Integer aisState;

    public final String aisStateText;

    public final String aisDestination;

    public final Timestamp movingSince;

    public final Timestamp stoppedSince;

    public final Timestamp inactiveSince;

    public ShipStateProperty(Timestamp timestamp, String posPrintable, PositionAccuracy posAccuracy,
                             PositionSource posSource, String posArea, Double speed, Double course, Double heading, Double aisDraught,
                             Integer aisState, String aisStateText, String aisDestination, Timestamp movingSince, Timestamp stoppedSince,
                             Timestamp inactiveSince) {
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
