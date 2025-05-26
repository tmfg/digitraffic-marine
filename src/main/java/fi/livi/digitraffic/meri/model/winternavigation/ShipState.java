package fi.livi.digitraffic.meri.model.winternavigation;

import java.time.Instant;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Entity
@DynamicUpdate
public class ShipState {

    @Id
    private String id;

    @JoinColumn(name = "vessel_pk")
    @OneToOne
    @MapsId
    private WinterNavigationShip winterNavigationShip;

    private Instant timestamp;

    private Double longitude;

    private Double latitude;

    private String posPrintable;

    private PositionAccuracy posAccuracy;

    private PositionSource posSource;

    private String posArea;

    private Double speed;

    private Double course;

    private Double heading;

    private Double aisDraught;

    private Integer aisState;

    private String aisStateText;

    private String aisDestination;

    private Instant movingSince;

    private Instant stoppedSince;

    private Instant inactiveSince;

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(final Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(final Double latitude) {
        this.latitude = latitude;
    }

    public String getPosPrintable() {
        return posPrintable;
    }

    public void setPosPrintable(final String posPrintable) {
        this.posPrintable = posPrintable;
    }

    public PositionAccuracy getPosAccuracy() {
        return posAccuracy;
    }

    public void setPosAccuracy(final PositionAccuracy posAccuracy) {
        this.posAccuracy = posAccuracy;
    }

    public PositionSource getPosSource() {
        return posSource;
    }

    public void setPosSource(final PositionSource posSource) {
        this.posSource = posSource;
    }

    public String getPosArea() {
        return posArea;
    }

    public void setPosArea(final String posArea) {
        this.posArea = posArea;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(final Double speed) {
        this.speed = speed;
    }

    public Double getCourse() {
        return course;
    }

    public void setCourse(final Double course) {
        this.course = course;
    }

    public Double getHeading() {
        return heading;
    }

    public void setHeading(final Double heading) {
        this.heading = heading;
    }

    public Double getAisDraught() {
        return aisDraught;
    }

    public void setAisDraught(final Double aisDraught) {
        this.aisDraught = aisDraught;
    }

    public Integer getAisState() {
        return aisState;
    }

    public void setAisState(final Integer aisState) {
        this.aisState = aisState;
    }

    public String getAisStateText() {
        return aisStateText;
    }

    public void setAisStateText(final String aisStateText) {
        this.aisStateText = aisStateText;
    }

    public String getAisDestination() {
        return aisDestination;
    }

    public void setAisDestination(final String aisDestination) {
        this.aisDestination = aisDestination;
    }

    public Instant getMovingSince() {
        return movingSince;
    }

    public void setMovingSince(final Instant movingSince) {
        this.movingSince = movingSince;
    }

    public Instant getStoppedSince() {
        return stoppedSince;
    }

    public void setStoppedSince(final Instant stoppedSince) {
        this.stoppedSince = stoppedSince;
    }

    public Instant getInactiveSince() {
        return inactiveSince;
    }

    public void setInactiveSince(final Instant inactiveSince) {
        this.inactiveSince = inactiveSince;
    }

    public void setWinterNavigationShip(final WinterNavigationShip winterNavigationShip) {
        this.winterNavigationShip = winterNavigationShip;
//        this.vesselPK = winterNavigationShip.getVesselPK();
    }
}
