package fi.livi.digitraffic.meri.model.portnet;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;

@ApiModel(description="Port call")
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface CargoInfoJson {
    Integer getCargoDischargeCode();
    String getCargoDescription();
    BigDecimal getCargoAmount();
}
