package fi.livi.digitraffic.meri.domain.winternavigation;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import fi.livi.digitraffic.meri.service.winternavigation.dto.PositionAccuracy;
import fi.livi.digitraffic.meri.service.winternavigation.dto.PositionSource;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
public class ShipState {

    @Id
    @Column(name = "vessel_pk", nullable = false)
    private String vesselPK;

    @OneToOne
    @JoinColumn(name = "vessel_pk", nullable = false)
    @MapsId
    private WinterNavigationShip winterNavigationShip;

    private ZonedDateTime timestamp;

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

    private ZonedDateTime movingSince;

    private ZonedDateTime stoppedSince;

    private ZonedDateTime inactiveSince;

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getPosPrintable() {
        return posPrintable;
    }

    public void setPosPrintable(String posPrintable) {
        this.posPrintable = posPrintable;
    }

    public PositionAccuracy getPosAccuracy() {
        return posAccuracy;
    }

    public void setPosAccuracy(PositionAccuracy posAccuracy) {
        this.posAccuracy = posAccuracy;
    }

    public PositionSource getPosSource() {
        return posSource;
    }

    public void setPosSource(PositionSource posSource) {
        this.posSource = posSource;
    }

    public String getPosArea() {
        return posArea;
    }

    public void setPosArea(String posArea) {
        this.posArea = posArea;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getCourse() {
        return course;
    }

    public void setCourse(Double course) {
        this.course = course;
    }

    public Double getHeading() {
        return heading;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }

    public Double getAisDraught() {
        return aisDraught;
    }

    public void setAisDraught(Double aisDraught) {
        this.aisDraught = aisDraught;
    }

    public Integer getAisState() {
        return aisState;
    }

    public void setAisState(Integer aisState) {
        this.aisState = aisState;
    }

    public String getAisStateText() {
        return aisStateText;
    }

    public void setAisStateText(String aisStateText) {
        this.aisStateText = aisStateText;
    }

    public String getAisDestination() {
        return aisDestination;
    }

    public void setAisDestination(String aisDestination) {
        this.aisDestination = aisDestination;
    }

    public ZonedDateTime getMovingSince() {
        return movingSince;
    }

    public void setMovingSince(ZonedDateTime movingSince) {
        this.movingSince = movingSince;
    }

    public ZonedDateTime getStoppedSince() {
        return stoppedSince;
    }

    public void setStoppedSince(ZonedDateTime stoppedSince) {
        this.stoppedSince = stoppedSince;
    }

    public ZonedDateTime getInactiveSince() {
        return inactiveSince;
    }

    public void setInactiveSince(ZonedDateTime inactiveSince) {
        this.inactiveSince = inactiveSince;
    }

    public void setWinterNavigationShip(final WinterNavigationShip winterNavigationShip) {
        this.winterNavigationShip = winterNavigationShip;
        this.vesselPK = winterNavigationShip.getVesselPK();
    }
}
