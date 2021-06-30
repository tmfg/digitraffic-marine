package fi.livi.digitraffic.meri.model.portnet.metadata;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.annotations.ApiModelProperty;

public class BerthProperties extends Properties {
    @ApiModelProperty(value = "Maritime Mobile Service Identity", required = true, position = 1)
    public final String locode;

    @ApiModelProperty(value = "Port area code", required = true)
    public final String portAreaCode;

    @ApiModelProperty(value = "Berth code", required = true)
    public final String berthCode;

    @ApiModelProperty(value = "Berth name", required = true)
    public final String berthName;

    public BerthProperties(final String locode, final String portAreaCode, final String berthCode, final String berthName) {
        this.locode = locode;
        this.portAreaCode = portAreaCode;
        this.berthCode = berthCode;
        this.berthName = berthName;
    }
}
