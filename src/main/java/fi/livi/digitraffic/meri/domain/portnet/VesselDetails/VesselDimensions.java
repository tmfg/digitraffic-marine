package fi.livi.digitraffic.meri.domain.portnet.VesselDetails;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

import fi.livi.digitraffic.meri.portnet.vesseldetails.xsd.VesselDetails;
import fi.livi.digitraffic.util.TypeUtil;

@Entity
@DynamicUpdate
public class VesselDimensions {

    @Id
    private Long vesselId;

    private String tonnageCertificateIssuer;

    private Timestamp dateOfIssue;

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

    private Timestamp keelDate;

    public void setAll(BigInteger vesselId, VesselDetails.Dimensions dimensions) {
        this.vesselId = vesselId.longValue();
        this.tonnageCertificateIssuer = dimensions.getTonnageCertificateIssuer();
        this.dateOfIssue = TypeUtil.getTimestamp(dimensions.getDateOfIssue());
        this.grossTonnage = TypeUtil.getInteger(dimensions.getGrossTonnage());
        this.netTonnage = TypeUtil.getInteger(dimensions.getNetTonnage());
        this.deathWeight = TypeUtil.getInteger(dimensions.getDeathWeight());
        this.length = TypeUtil.getDouble(dimensions.getLength());
        this.overallLength = TypeUtil.getDouble(dimensions.getOverallLength());
        this.height = TypeUtil.getDouble(dimensions.getHeight());
        this.breadth = TypeUtil.getDouble(dimensions.getBreadth());
        this.draught = TypeUtil.getDouble(dimensions.getDraught());
        this.maxSpeed = TypeUtil.getDouble(dimensions.getMaxSpeed());
        this.enginePower = dimensions.getEnginePower();
        this.totalPower = TypeUtil.getInteger(dimensions.getTotalPower());
        this.maxPersons = TypeUtil.getInteger(dimensions.getMaxPersons());
        this.maxPassengers = TypeUtil.getInteger(dimensions.getMaxPassengers());
        this.keelDate = TypeUtil.getTimestamp(dimensions.getKeelDate());
    }

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

    public Timestamp getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(Timestamp dateOfIssue) {
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

    public Timestamp getKeelDate() {
        return keelDate;
    }

    public void setKeelDate(Timestamp keelDate) {
        this.keelDate = keelDate;
    }
}
