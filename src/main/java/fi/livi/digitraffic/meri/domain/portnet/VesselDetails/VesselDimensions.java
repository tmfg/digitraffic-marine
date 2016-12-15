package fi.livi.digitraffic.meri.domain.portnet.VesselDetails;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
public class VesselDimensions {

    @Id
    private Long vesselId;

    private String tonnageCertificateIssuer;

    private ZonedDateTime dateOfIssue;

    private Integer grossTonnage;

    private Integer netTonnage;

    private Integer deathWeight;

    private Double length;

    private Double overallLength;

    private Double height;

    private Double breadth;

    private Double draught;

    private Double maxSpeed;

    private String enginePower;

    private Integer totalPower;

    private Integer maxPersons;

    private Integer maxPassengers;

    private ZonedDateTime keelDate;

    public Long getVesselId() {
        return vesselId;
    }

    public void setVesselId(Long vesselId) {
        this.vesselId = vesselId;
    }

    public String getTonnageCertificateIssuer() {
        return tonnageCertificateIssuer;
    }

    public void setTonnageCertificateIssuer(String tonnageCertificateIssuer) {
        this.tonnageCertificateIssuer = tonnageCertificateIssuer;
    }

    public ZonedDateTime getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(ZonedDateTime dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public Integer getGrossTonnage() {
        return grossTonnage;
    }

    public void setGrossTonnage(Integer grossTonnage) {
        this.grossTonnage = grossTonnage;
    }

    public Integer getNetTonnage() {
        return netTonnage;
    }

    public void setNetTonnage(Integer netTonnage) {
        this.netTonnage = netTonnage;
    }

    public Integer getDeathWeight() {
        return deathWeight;
    }

    public void setDeathWeight(Integer deathWeight) {
        this.deathWeight = deathWeight;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getOverallLength() {
        return overallLength;
    }

    public void setOverallLength(Double overallLength) {
        this.overallLength = overallLength;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getBreadth() {
        return breadth;
    }

    public void setBreadth(Double breadth) {
        this.breadth = breadth;
    }

    public Double getDraught() {
        return draught;
    }

    public void setDraught(Double draught) {
        this.draught = draught;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(String enginePower) {
        this.enginePower = enginePower;
    }

    public Integer getTotalPower() {
        return totalPower;
    }

    public void setTotalPower(Integer totalPower) {
        this.totalPower = totalPower;
    }

    public Integer getMaxPersons() {
        return maxPersons;
    }

    public void setMaxPersons(Integer maxPersons) {
        this.maxPersons = maxPersons;
    }

    public Integer getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(Integer maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    public ZonedDateTime getKeelDate() {
        return keelDate;
    }

    public void setKeelDate(ZonedDateTime keelDate) {
        this.keelDate = keelDate;
    }
}
