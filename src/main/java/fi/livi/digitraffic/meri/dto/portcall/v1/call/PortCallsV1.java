package fi.livi.digitraffic.meri.dto.portcall.v1.call;

import java.time.Instant;
import java.util.List;

import fi.livi.digitraffic.meri.dto.LastModifiedSupport;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Port call")
public final class PortCallsV1 implements LastModifiedSupport {
    @Schema(description = "Timestamp when port calls were updated", requiredMode = Schema.RequiredMode.REQUIRED)
    public final Instant dataUpdatedTime;

    @Schema(description = "Port calls", requiredMode = Schema.RequiredMode.REQUIRED)
    public final List<PortCallJsonV1> portCalls;

    public PortCallsV1(final Instant dataUpdatedTime, final List<PortCallJsonV1> portCalls) {
        this.dataUpdatedTime = dataUpdatedTime;
        this.portCalls = portCalls;
    }

    @Override
    public Instant getLastModified() {
        return dataUpdatedTime;
    }
}
