package fi.livi.digitraffic.meri.model.portnet.metadata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Ssn location")
@JsonPropertyOrder({ "locode", "locationName", "country", "wgs84Lat", "wgs84Long"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface SsnLocationJson {
    @ApiModelProperty(value = "United Nations Code for Trade and Transport Locations", required = true)
    String getLocode();

    @ApiModelProperty(value = "Location name", required = true)
    String getLocationName();

    @ApiModelProperty(value = "Country", required = true)
    String getCountry();

    @ApiModelProperty(value = "Wgs84 latitude", required = false)
    Double getWgs84Lat();

    @ApiModelProperty(value = "Wgs84 longitude", required = false)
    Double getWgs84Long();
}
