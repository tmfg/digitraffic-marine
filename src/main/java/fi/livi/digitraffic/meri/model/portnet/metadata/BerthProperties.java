package fi.livi.digitraffic.meri.model.portnet.metadata;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.annotations.ApiModelProperty;

public class BerthProperties extends Properties {
    @ApiModelProperty(value = "Maritime Mobile Service Identity", required = true, position = 1)
    public final String locode;

    @ApiModelProperty(value = "Berth name", required = true)
    public final String berthName;

    public BerthProperties(final String locode, final String berthName) {
        this.locode = locode;
        this.berthName = berthName;
    }
}
