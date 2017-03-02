package fi.livi.digitraffic.meri.model.portnet.metadata;

import io.swagger.annotations.ApiModelProperty;

public class PortAreaProperties {
    @ApiModelProperty(value = "Port area name", required = true)
    public final String portAreaName;

    public PortAreaProperties(final String portAreaName) {
        this.portAreaName = portAreaName;
    }
}
