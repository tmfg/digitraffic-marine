package fi.livi.digitraffic.meri.model.portnet.metadata;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(parent = Properties.class)
public class BerthProperties extends Properties {
    @ApiModelProperty(value = "Berth name", required = true)
    public final String berthName;

    public BerthProperties(final String berthName) {
        this.berthName = berthName;
    }
}
