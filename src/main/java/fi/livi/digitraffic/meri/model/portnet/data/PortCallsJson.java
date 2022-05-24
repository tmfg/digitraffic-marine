package fi.livi.digitraffic.meri.model.portnet.data;

import java.time.ZonedDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Port call")
public final class PortCallsJson {
    @Schema(description = "Timestamp when port calls were updated", required = true)
    public final ZonedDateTime portCallsUpdated;

    @Schema(description = "Port calls", required = true)
    public final List<PortCallJson> portCalls;

    public PortCallsJson(final ZonedDateTime portCallsUpdated, final List<PortCallJson> portCalls) {
        this.portCallsUpdated = portCallsUpdated;
        this.portCalls = portCalls;
    }
}
