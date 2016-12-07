package fi.livi.digitraffic.meri.model.portnet.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Agent info")
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface AgentInfoJson {

    @ApiModelProperty(value = "Role of the agent")
    Integer getRole();

    @ApiModelProperty(value = "Port call direction")
    String getPortCallDirection();

    @ApiModelProperty(value = "Name of the agent")
    String getName();

    @ApiModelProperty(value = "Electronic data interchange number")
    String getEdiNumber();
}
