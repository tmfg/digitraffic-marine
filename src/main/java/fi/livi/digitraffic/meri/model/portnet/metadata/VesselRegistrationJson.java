package fi.livi.digitraffic.meri.model.portnet.metadata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Vessel registration", value = "VesselRegistration")
@JsonPropertyOrder({ "vesselId", "nationality", "portOfRegistry", "domicile" })
public interface VesselRegistrationJson {

    @JsonIgnore
    Long getVesselId();

    @ApiModelProperty(value = "Ship nationality")
    String getNationality();

    @ApiModelProperty(value = "Ship home city")
    String getPortOfRegistry();

    @ApiModelProperty(value = "Always null")
    @JsonIgnore
    String getDomicile();
}
