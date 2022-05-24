package fi.livi.digitraffic.meri.model.v2;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Description of code")
public interface V2CodeDescription {
    @Schema(description = "Code", required = true)
    String getCode();
    @Schema(description = "Description of the code(Finnish)", required = true)
    String getDescriptionFi();
    @Schema(description = "Description of the code(English", required = true)
    String getDescriptionEn();
}
