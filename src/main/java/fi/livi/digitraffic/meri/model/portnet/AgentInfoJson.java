package fi.livi.digitraffic.meri.model.portnet;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;

@ApiModel(description="Agent info")
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface AgentInfoJson {
    Integer getRole();
    String getPortCallDirection();
    String getName();
    String getEditNumber();
}
