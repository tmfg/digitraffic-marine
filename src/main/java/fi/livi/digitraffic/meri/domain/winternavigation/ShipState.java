package fi.livi.digitraffic.meri.domain.winternavigation;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import fi.livi.digitraffic.meri.service.winternavigation.dto.PositionAccuracy;
import fi.livi.digitraffic.meri.service.winternavigation.dto.PositionSource;

@Entity
public class ShipState {

    @Id
    @Column(name = "vessel_pk", nullable = false)
    private String vesselPK;

    private Timestamp timestamp;

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

    private Timestamp movingSince;

    private Timestamp stoppedSince;

    private Timestamp inactiveSince;

    public String getVesselPK() {
        return vesselPK;
    }

    public void setVesselPK(String vesselPK) {
        this.vesselPK = vesselPK;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
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

    public Timestamp getMovingSince() {
        return movingSince;
    }

    public void setMovingSince(Timestamp movingSince) {
        this.movingSince = movingSince;
    }

    public Timestamp getStoppedSince() {
        return stoppedSince;
    }

    public void setStoppedSince(Timestamp stoppedSince) {
        this.stoppedSince = stoppedSince;
    }

    public Timestamp getInactiveSince() {
        return inactiveSince;
    }

    public void setInactiveSince(Timestamp inactiveSince) {
        this.inactiveSince = inactiveSince;
    }
}
