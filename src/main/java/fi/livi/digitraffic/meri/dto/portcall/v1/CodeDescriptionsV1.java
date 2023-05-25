package fi.livi.digitraffic.meri.dto.portcall.v1;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableList;

import fi.livi.digitraffic.meri.dto.LastModifiedSupport;
import fi.livi.digitraffic.meri.model.portnet.data.CodeDescriptionJson;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Code descriptions associated with port calls")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class CodeDescriptionsV1 implements LastModifiedSupport {
    @Schema(description = "Timestamp when metadata were last updated", required = true)
    public final Instant dataUpdatedTime;

    @Schema(description = "All cargo type descriptions", requiredMode = Schema.RequiredMode.REQUIRED)
    public final List<CodeDescriptionJson> cargoTypes;
    @Schema(description = "All vessel type descriptions", requiredMode = Schema.RequiredMode.REQUIRED)
    public final List<CodeDescriptionJson> vesselTypes;
    @Schema(description = "All agent type descriptions", requiredMode = Schema.RequiredMode.REQUIRED)
    public final List<CodeDescriptionJson> agentTypes;

    public CodeDescriptionsV1(final Instant dataUpdatedTime,
                              final List<CodeDescriptionJson> cargoTypes,
                              final List<CodeDescriptionJson> vesselTypes,
                              final List<CodeDescriptionJson> agentTypes) {
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
