package fi.livi.digitraffic.meri.dto.portcall.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableList;
import fi.livi.digitraffic.meri.model.v2.V2CodeDescription;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description="Code descriptions associated with port calls")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CodeDescriptionsV1 {
    @Schema(description = "Timestamp when metadata were last updated", required = true)
    public final Instant dataUpdatedTime;

    @Schema(description = "All cargo type descriptions", required = true)
    public final List<V2CodeDescription> cargoTypes;
    @Schema(description = "All vessel type descriptions", required = true)
    public final List<V2CodeDescription> vesselTypes;
    @Schema(description = "All agent type descriptions", required = true)
    public final List<V2CodeDescription> agentTypes;

    public CodeDescriptionsV1(final Instant dataUpdatedTime,
                               final List<V2CodeDescription> cargoTypes,
                               final List<V2CodeDescription> vesselTypes,
                               final List<V2CodeDescription> agentTypes) {
        this.dataUpdatedTime = dataUpdatedTime;
        this.cargoTypes = ImmutableList.copyOf(cargoTypes);
        this.vesselTypes = ImmutableList.copyOf(vesselTypes);
        this.agentTypes = ImmutableList.copyOf(agentTypes);
    }
}
