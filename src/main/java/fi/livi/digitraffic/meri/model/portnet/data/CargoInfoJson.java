package fi.livi.digitraffic.meri.model.portnet.data;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Port call")
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface CargoInfoJson {

    @ApiModelProperty(value = "Cargo discharge code")
    Integer getCargoDischargeCode();

    @ApiModelProperty(value = "Cargo description")
    String getCargoDescription();

    @ApiModelProperty(value = "Cargo amount")
    BigDecimal getCargoAmount();
}
