package fi.livi.digitraffic.meri.model.portnet.metadata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Vessel system", value = "vesselSystem")
@JsonPropertyOrder({ "vesselId", "shipOwner", "shipTelephone1", "shipTelephone2", "shipFax", "shipEmail", "shipVerifier" })
public interface VesselSystemJson {

    @JsonIgnore
    Long getVesselId();

    @ApiModelProperty(value = "Ship owner name")
    String getShipOwner();

    @ApiModelProperty(value = "Ship telephone")
    String getShipTelephone1();

    @ApiModelProperty(value = "Always null")
    @JsonIgnore
    String getShipTelephone2();

    @ApiModelProperty(value = "Always null")
    @JsonIgnore
    String getShipFax();

    @ApiModelProperty(value = "Ship email address")
    String getShipEmail();

    @ApiModelProperty(value = "Ship verifier")
    String getShipVerifier();
}
