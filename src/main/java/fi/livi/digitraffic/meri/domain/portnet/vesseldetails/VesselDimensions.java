package fi.livi.digitraffic.meri.domain.portnet.vesseldetails;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.portnet.vesseldetails.xsd.VesselDetails;
import fi.livi.digitraffic.meri.util.TypeUtil;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Vessel dimensions", name = "VesselDimensions")
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

    @Schema(description = "Tonnage certificate issuer")
    private String tonnageCertificateIssuer;

    @Schema(description = "Date when tonnage certificate was issued")
    private Timestamp dateOfIssue;

    @Schema(description = "Ship gross tonnage")
    private Integer grossTonnage;

    @Schema(description = "Ship net tonnage")
    private Integer netTonnage;

    @Schema(description = "Ship death weight")
    private Integer deathWeight;

    @Schema(description = "Ship length")
    private Double length;

    @Schema(description = "Ship overall length")
    private Double overallLength;

    @Schema(description = "Ship height")
    private Double height;

    @Schema(description = "Ship breadth")
    private Double breadth;

    @Schema(description = "Ship draught")
    private Double draught;

    @Schema(description = "Ship max speed")
    private Double maxSpeed;

    @Schema(description = "Ship engine power")
    private String enginePower;

    @Schema(description = "Always null")
    @JsonIgnore
    private Integer totalPower;

    @Schema(description = "Always null")
    @JsonIgnore
    private Integer maxPersons;

    @Schema(description = "Always null")
    @JsonIgnore
    private Integer maxPassengers;

    @Schema(description = "Always null")
    @JsonIgnore
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
