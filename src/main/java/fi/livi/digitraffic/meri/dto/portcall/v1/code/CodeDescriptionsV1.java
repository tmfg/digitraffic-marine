package fi.livi.digitraffic.meri.dto.portcall.v1.code;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableList;

import fi.livi.digitraffic.common.dto.LastModifiedSupport;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Code descriptions associated with port calls")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class CodeDescriptionsV1 implements LastModifiedSupport {
    @Schema(description = "Timestamp when metadata were last updated", requiredMode = Schema.RequiredMode.REQUIRED)
    public final Instant dataUpdatedTime;

    @Schema(description = "All cargo type descriptions", requiredMode = Schema.RequiredMode.REQUIRED)
    public final List<CodeDescriptionJsonV1> cargoTypes;
    @Schema(description = "All vessel type descriptions", requiredMode = Schema.RequiredMode.REQUIRED)
    public final List<CodeDescriptionJsonV1> vesselTypes;
    @Schema(description = "All agent type descriptions", requiredMode = Schema.RequiredMode.REQUIRED)
    public final List<CodeDescriptionJsonV1> agentTypes;

    public CodeDescriptionsV1(final Instant dataUpdatedTime,
                              final List<CodeDescriptionJsonV1> cargoTypes,
                              final List<CodeDescriptionJsonV1> vesselTypes,
                              final List<CodeDescriptionJsonV1> agentTypes) {
        this.dataUpdatedTime = dataUpdatedTime;
        this.cargoTypes = ImmutableList.copyOf(cargoTypes);
        this.vesselTypes = ImmutableList.copyOf(vesselTypes);
        this.agentTypes = ImmutableList.copyOf(agentTypes);
    }

    @Override
    public Instant getLastModified() {
        return dataUpdatedTime;
    }
}
