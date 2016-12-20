package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Vessel construction")
@JsonPropertyOrder({ "vesselId", "vesselTypeCode", "vesselTypeName", "iceClassCode", "iceClassIssueDate", "iceClassIssuePlace",
                     "iceClassEndDate", "classificationSociety", "doubleBottom", "inertGasSystem", "ballastTank" })
public interface VesselConstructionJson {

    @JsonIgnore
    Long getVesselId();

    @ApiModelProperty(value = "Ship Vessel Type Code")
    Integer getVesselTypeCode();

    @ApiModelProperty(value = "Ship Vessel Type Name")
    String getVesselTypeName();

    @ApiModelProperty(value = "Ship ice class code")
    String getIceClassCode();

    @ApiModelProperty(value = "Date when ice class endorsement was issued")
    Timestamp getIceClassIssueDate();

    @ApiModelProperty(value = "Place where ice class endorsement was issued")
    String getIceClassIssuePlace();

    @ApiModelProperty(value = "Date when ice class endorsement ends")
    Timestamp getIceClassEndDate();

    @ApiModelProperty(value = "Always null")
    @JsonIgnore
    String getClassificationSociety();

    @ApiModelProperty(value = "Tells whether vessel has a double bottom")
    Boolean getDoubleBottom();

    @ApiModelProperty(value = "Tells whether vessel has an Inert gas system")
    Boolean getInertGasSystem();

    @ApiModelProperty(value = "Tells whether vessel has a ballast tank")
    Boolean getBallastTank();
}
