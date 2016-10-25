package fi.livi.digitraffic.meri.model.portnet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Port area")
public interface PortAreaJson {
    @ApiModelProperty(value = "UN/LOCODE", required = true)
    String getLocode();

    @ApiModelProperty(value = "Port area code", required = true)
    String getPortAreaCode();

    @ApiModelProperty(value = "Port area name", required = true)
    String getPortAreaName();
}
