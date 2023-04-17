package fi.livi.digitraffic.meri.dto.portcall.v1;

import java.time.Instant;
import java.util.List;

import fi.livi.digitraffic.meri.dto.LastModifiedSupport;
import fi.livi.digitraffic.meri.model.portnet.data.PortCallJson;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Port call")
public final class PortCallsV1 implements LastModifiedSupport {
    @Schema(description = "Timestamp when port calls were updated", required = true)
    public final Instant dataUpdatedTime;

    @Schema(description = "Port calls", required = true)
    public final List<PortCallJson> portCalls;

    public PortCallsV1(final Instant dataUpdatedTime, final List<PortCallJson> portCalls) {
        this.dataUpdatedTime = dataUpdatedTime;
        this.portCalls = portCalls;
    }

    @Override
    public Instant getLastModified() {
        return dataUpdatedTime;
    }
}
