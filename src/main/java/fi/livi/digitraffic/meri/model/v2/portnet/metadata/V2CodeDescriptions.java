package fi.livi.digitraffic.meri.model.v2.portnet.metadata;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableList;
import fi.livi.digitraffic.meri.model.v2.V2CodeDescription;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Code descriptions associated with port calls")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class V2CodeDescriptions {
    @Schema(description = "Timestamp when metadata were last updated", required = true)
    public final ZonedDateTime lastUpdated;

    @Schema(description = "All cargo type descriptions", required = true)
    public final List<V2CodeDescription> cargoTypes;
    @Schema(description = "All vessel type descriptions", required = true)
    public final List<V2CodeDescription> vesselTypes;
    @Schema(description = "All agent type descriptions", required = true)
    public final List<V2CodeDescription> agentTypes;

    public V2CodeDescriptions(final ZonedDateTime lastUpdated,
                            final List<V2CodeDescription> cargoTypes, final List<V2CodeDescription> vesselTypes,
                            final List<V2CodeDescription> agentTypes) {
        this.lastUpdated = lastUpdated;
        this.cargoTypes = ImmutableList.copyOf(cargoTypes);
        this.vesselTypes = ImmutableList.copyOf(vesselTypes);
        this.agentTypes = ImmutableList.copyOf(agentTypes);
    }
}
