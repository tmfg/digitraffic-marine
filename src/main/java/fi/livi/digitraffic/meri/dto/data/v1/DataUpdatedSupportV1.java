package fi.livi.digitraffic.meri.dto.data.v1;

import java.time.Instant;

import fi.livi.digitraffic.meri.dto.LastModifiedSupport;
import io.swagger.v3.oas.annotations.media.Schema;

public interface DataUpdatedSupportV1 extends LastModifiedSupport {

    @Schema(description = "Data last updated time", required = true)
    Instant getDataUpdatedTime();

    @Override
    default Instant getLastModified() {
        return getDataUpdatedTime();
    }
}
