package fi.livi.digitraffic.meri.model.portnet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Berth")
public interface BerthJson {
    @ApiModelProperty(value = "Locode", required = true)
    String getLocode();

    @ApiModelProperty(value = "Port area code", required = true)
    String getPortAreaCode();

    @ApiModelProperty(value = "Berth code", required = true)
    String getBerthCode();

    @ApiModelProperty(value = "Berth name", required = true)
    String getBerthName();
}
