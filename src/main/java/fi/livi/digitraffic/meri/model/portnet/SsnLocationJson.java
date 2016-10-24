package fi.livi.digitraffic.meri.model.portnet;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Ssn location")
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface SsnLocationJson {
    @ApiModelProperty(value = "United Nations Code for Trade and Transport Locations", required = true)
    String getLocode();

    @ApiModelProperty(value = "Location name", required = true)
    String getLocationName();

    @ApiModelProperty(value = "Wgs84 latitude", required = false)
    Double getWgs84Lat();

    @ApiModelProperty(value = "Wgs84 longitude", required = false)
    Double getWgs84Long();
}
