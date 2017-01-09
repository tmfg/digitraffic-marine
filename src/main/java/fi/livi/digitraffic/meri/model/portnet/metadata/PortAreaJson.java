package fi.livi.digitraffic.meri.model.portnet.metadata;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Port area")
@JsonPropertyOrder({ "locode", "portAreaCode", "portAreaName"})
public interface PortAreaJson {
    @ApiModelProperty(value = "UN/LOCODE", required = true)
    @Value("#{target.portAreaKey.locode}")
    String getLocode();

    @ApiModelProperty(value = "Port area code", required = true)
    @Value("#{target.portAreaKey.portAreaCode}")
    String getPortAreaCode();

    @ApiModelProperty(value = "Port area name", required = true)
    String getPortAreaName();

    @ApiModelProperty(value = "Wgs84 latitude")
    Double getWgs84Lat();

    @ApiModelProperty(value = "Wgs84 longitude")
    Double getWgs84Long();
}
