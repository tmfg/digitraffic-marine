package fi.livi.digitraffic.meri.dto.portcall.v1.code;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Description of code")
public interface CodeDescriptionJsonV1 {
    @Schema(description = "Code", requiredMode = Schema.RequiredMode.REQUIRED)
    String getCode();
    @Schema(description = "Description of the code(Finnish)", requiredMode = Schema.RequiredMode.REQUIRED)
    String getDescriptionFi();
    @Schema(description = "Description of the code(English)", requiredMode = Schema.RequiredMode.REQUIRED)
    String getDescriptionEn();
}
