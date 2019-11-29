package fi.livi.digitraffic.meri.model.v2;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Description of code")
public interface V2CodeDescription {
    @ApiModelProperty(value = "Code", required = true)
    String getCode();
    @ApiModelProperty(value = "Description of the code(Finnish)", required = true)
    String getDescriptionFi();
    @ApiModelProperty(value = "Description of the code(English", required = true)
    String getDescriptionEn();
}
