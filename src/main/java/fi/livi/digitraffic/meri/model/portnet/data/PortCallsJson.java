package fi.livi.digitraffic.meri.model.portnet.data;

import java.time.ZonedDateTime;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Port call")
public final class PortCallsJson {
    @ApiModelProperty(value = "Timestamp when port calls were updated", required = true)
    public final ZonedDateTime portCallsUpdated;

    @ApiModelProperty(value = "Port calls", required = true)
    public final List<PortCallJson> portCalls;

    public PortCallsJson(final ZonedDateTime portCallsUpdated, final List<PortCallJson> portCalls) {
        this.portCallsUpdated = portCallsUpdated;
        this.portCalls = portCalls;
    }
}
