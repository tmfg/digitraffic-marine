package fi.livi.digitraffic.meri.dto.data.v1;

import java.time.Instant;

import fi.livi.digitraffic.common.dto.LastModifiedSupport;
import io.swagger.v3.oas.annotations.media.Schema;

public interface DataUpdatedSupportV1 extends LastModifiedSupport {

    @Schema(description = "Data last updated time", requiredMode = Schema.RequiredMode.REQUIRED)
    Instant getDataUpdatedTime();

    @Override
    default Instant getLastModified() {
        return getDataUpdatedTime();
    }
}
