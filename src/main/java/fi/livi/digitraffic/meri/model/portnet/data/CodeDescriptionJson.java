package fi.livi.digitraffic.meri.model.portnet.data;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Description of code")
public interface CodeDescriptionJson {
    @Schema(description = "Code", requiredMode = Schema.RequiredMode.REQUIRED)
    String getCode();
    @Schema(description = "Description of the code(Finnish)", requiredMode = Schema.RequiredMode.REQUIRED)
    String getDescriptionFi();
    @Schema(description = "Description of the code(English)", requiredMode = Schema.RequiredMode.REQUIRED)
    String getDescriptionEn();
}
