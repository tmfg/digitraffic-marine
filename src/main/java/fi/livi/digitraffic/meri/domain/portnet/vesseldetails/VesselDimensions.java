package fi.livi.digitraffic.meri.domain.portnet.vesseldetails;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import fi.livi.digitraffic.meri.portnet.xsd.Dimensions;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.util.TypeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Vessel dimensions", value = "VesselDimensions")
@JsonPropertyOrder({ "vesselId", "tonnageCertificateIssuer", "dateOfIssue", "grossTonnage", "netTonnage", "deathWeight", "length",
                     "overallLength", "height", "breadth", "draught", "maxSpeed", "enginePower", "totalPower", "maxPersons",
                     "maxPassengers", "keelDate" })
@Entity
@DynamicUpdate
public class VesselDimensions {

    @JsonIgnore
    @Id
    private Long vesselId;

    @JsonIgnore
    @OneToOne(targetEntity = fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails.class)
    @JoinColumn(name = "vesselId", nullable = false)
    @MapsId
    private fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails vesselDetails;

    @ApiModelProperty(value = "Tonnage certificate issuer")
    private String tonnageCertificateIssuer;

    @ApiModelProperty(value = "Date when tonnage certificate was issued")
    private Timestamp dateOfIssue;

    @ApiModelProperty(value = "Ship gross tonnage")
    private Integer grossTonnage;

    @ApiModelProperty(value = "Ship net tonnage")
    private Integer netTonnage;

    @ApiModelProperty(value = "Ship death weight")
    private Integer deathWeight;

    @ApiModelProperty(value = "Ship length")
    private Double length;

    @ApiModelProperty(value = "Ship overall length")
    private Double overallLength;

    @ApiModelProperty(value = "Ship height")
    private Double height;

    @ApiModelProperty(value = "Ship breadth")
    private Double breadth;

    @ApiModelProperty(value = "Ship draught")
    private Double draught;

    @ApiModelProperty(value = "Ship max speed")
    private Double maxSpeed;

    @ApiModelProperty(value = "Ship engine power")
    private String enginePower;

    @ApiModelProperty(value = "Always null")
    @JsonIgnore
    private Integer totalPower;

    @ApiModelProperty(value = "Always null")
    @JsonIgnore
    private Integer maxPersons;

    @ApiModelProperty(value = "Always null")
    @JsonIgnore
    private Integer maxPassengers;

    @ApiModelProperty(value = "Always null")
    @JsonIgnore
    private Timestamp keelDate;

    public void setAll(final BigInteger vesselId, final Dimensions dimensions) {
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

    public String getTonnageCertificateIssuer() {
        return tonnageCertificateIssuer;
    }

    public Timestamp getDateOfIssue() {
        return dateOfIssue;
    }

    public Integer getGrossTonnage() {
        return grossTonnage;
    }

    public Integer getNetTonnage() {
        return netTonnage;
    }

    public Integer getDeathWeight() {
        return deathWeight;
    }

    public Double getLength() {
        return length;
    }

    public Double getOverallLength() {
        return overallLength;
    }

    public Double getHeight() {
        return height;
    }

    public Double getBreadth() {
        return breadth;
    }

    public Double getDraught() {
        return draught;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public String getEnginePower() {
        return enginePower;
    }

    public Integer getTotalPower() {
        return totalPower;
    }

    public Integer getMaxPersons() {
        return maxPersons;
    }

    public Integer getMaxPassengers() {
        return maxPassengers;
    }

    public Timestamp getKeelDate() {
        return keelDate;
    }

    public void setVesselDetails(final fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails vesselDetails) {
        this.vesselDetails = vesselDetails;
    }
}
