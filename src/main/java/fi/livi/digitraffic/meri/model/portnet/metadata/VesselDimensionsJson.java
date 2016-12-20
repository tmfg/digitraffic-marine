package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Vessel dimensions", value = "vesselDimensions")
@JsonPropertyOrder({ "vesselId", "tonnageCertificateIssuer", "dateOfIssue", "grossTonnage", "netTonnage", "deathWeight", "length",
                     "overallLength", "height", "breadth", "draught", "maxSpeed", "enginePower", "totalPower", "maxPersons",
                     "maxPassengers", "keelDate" })
public interface VesselDimensionsJson {

    @JsonIgnore
    Long getVesselId();

    @ApiModelProperty(value = "Tonnage certificate issuer")
    String getTonnageCertificateIssuer();

    @ApiModelProperty(value = "Date when tonnage certificate was issued")
    Timestamp getDateOfIssue();

    @ApiModelProperty(value = "Ship gross tonnage")
    Integer getGrossTonnage();

    @ApiModelProperty(value = "Ship net tonnage")
    Integer getNetTonnage();

    @ApiModelProperty(value = "Ship death weight")
    Integer getDeathWeight();

    @ApiModelProperty(value = "Ship length")
    Double getLength();

    @ApiModelProperty(value = "Ship overall length")
    Double getOverallLength();

    @ApiModelProperty(value = "Ship height")
    Double getHeight();

    @ApiModelProperty(value = "Ship breadth")
    Double getBreadth();

    @ApiModelProperty(value = "Ship draught")
    Double getDraught();

    @ApiModelProperty(value = "Ship max speed")
    Double getMaxSpeed();

    @ApiModelProperty(value = "Ship engine power")
    String getEnginePower();

    @ApiModelProperty(value = "Always null")
    @JsonIgnore
    Integer getTotalPower();

    @ApiModelProperty(value = "Always null")
    @JsonIgnore
    Integer getMaxPersons();

    @ApiModelProperty(value = "Always null")
    @JsonIgnore
    Integer getMaxPassengers();

    @ApiModelProperty(value = "Always null")
    @JsonIgnore
    Timestamp getKeelDate();
}
