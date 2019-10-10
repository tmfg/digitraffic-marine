package fi.livi.digitraffic.meri.model.portnet.metadata;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.annotations.ApiModelProperty;

public class PortAreaProperties extends Properties {
    @ApiModelProperty(value = "Maritime Mobile Service Identity", required = true, position = 1)
    public final String locode;

    @ApiModelProperty(value = "Port area name", required = true)
    public final String portAreaName;

    public PortAreaProperties(final String locode, final String portAreaName) {
        this.locode = locode;
        this.portAreaName = portAreaName;
    }
}
