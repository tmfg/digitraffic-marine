package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableList;
import fi.livi.digitraffic.meri.model.CodeDescriptionJson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Code descriptions associated with port calls")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class CodeDescriptions {
    @ApiModelProperty(value = "Timestamp when metadata were last updated", required = true)
    public final ZonedDateTime lastUpdated;

    @ApiModelProperty(value = "All cargo type descriptions", required = true)
    public final List<CodeDescriptionJson> cargoTypes;
    @ApiModelProperty(value = "All vessel type descriptions", required = true)
    public final List<CodeDescriptionJson> vesselTypes;
    @ApiModelProperty(value = "All agent type descriptions", required = true)
    public final List<CodeDescriptionJson> agentTypes;

    public CodeDescriptions(final ZonedDateTime lastUpdated,
                            final List<CodeDescriptionJson> cargoTypes, final List<CodeDescriptionJson> vesselTypes,
                            final List<CodeDescriptionJson> agentTypes) {
        this.lastUpdated = lastUpdated;
        this.cargoTypes = ImmutableList.copyOf(cargoTypes);
        this.vesselTypes = ImmutableList.copyOf(vesselTypes);
        this.agentTypes = ImmutableList.copyOf(agentTypes);
    }
}
