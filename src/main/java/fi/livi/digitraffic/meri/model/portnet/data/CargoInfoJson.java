package fi.livi.digitraffic.meri.model.portnet.data;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Port call")
public interface CargoInfoJson {

    @Schema(description = "Cargo discharge code")
    Integer getCargoDischargeCode();

    @Schema(description = "Cargo description")
    String getCargoDescription();

    @Schema(description = "Cargo amount")
    BigDecimal getCargoAmount();
}
