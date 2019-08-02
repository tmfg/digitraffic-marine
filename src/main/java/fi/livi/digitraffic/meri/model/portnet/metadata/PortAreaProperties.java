package fi.livi.digitraffic.meri.model.portnet.metadata;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(parent = Properties.class)
public class PortAreaProperties extends Properties {
    @ApiModelProperty(value = "Port area name", required = true)
    public final String portAreaName;

    public PortAreaProperties(final String portAreaName) {
        this.portAreaName = portAreaName;
    }
}
