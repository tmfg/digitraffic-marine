package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableList;

import fi.livi.digitraffic.meri.model.CodeDescription;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Code descriptions associated with port calls")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class CodeDescriptions_V1 {
    @Schema(description = "Timestamp when metadata were last updated", required = true)
    public final ZonedDateTime lastUpdated;

    @Schema(description = "All cargo type descriptions", required = true)
    public final List<CodeDescription> cargoTypes;
    @Schema(description = "All vessel type descriptions", required = true)
    public final List<CodeDescription> vesselTypes;
    @Schema(description = "All agent type descriptions", required = true)
    public final List<CodeDescription> agentTypes;

    public CodeDescriptions_V1(final ZonedDateTime lastUpdated,
                               final List<CodeDescription> cargoTypes, final List<CodeDescription> vesselTypes,
                               final List<CodeDescription> agentTypes) {
        this.lastUpdated = lastUpdated;
        this.cargoTypes = ImmutableList.copyOf(cargoTypes);
        this.vesselTypes = ImmutableList.copyOf(vesselTypes);
        this.agentTypes = ImmutableList.copyOf(agentTypes);
    }
}
