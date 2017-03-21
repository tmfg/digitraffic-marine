package fi.livi.digitraffic.meri.model.portnet.metadata;

import io.swagger.annotations.ApiModelProperty;

public class BerthProperties {
    @ApiModelProperty(value = "Berth name", required = true)
    public final String berthName;

    public BerthProperties(final String berthName) {
        this.berthName = berthName;
    }
}
