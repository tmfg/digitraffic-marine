package fi.livi.digitraffic.meri.model.portnet.metadata;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortAreaProperties {
    @ApiModelProperty(value = "Port area name", required = true)
    public final String portAreaName;

    public PortAreaProperties(final String portAreaName) {
        this.portAreaName = portAreaName;
    }
}
