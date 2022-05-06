package fi.livi.digitraffic.meri.model.portnet.data;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Agent info")
public interface AgentInfoJson {

    @Schema(description = "Role of the agent")
    Integer getRole();

    @Schema(description = "Port call direction")
    String getPortCallDirection();

    @Schema(description = "Name of the agent")
    String getName();

    @Schema(description = "Electronic data interchange number")
    String getEdiNumber();
}
