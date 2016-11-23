package fi.livi.digitraffic.meri.model.portnet.metadata;

import static java.util.Collections.emptyList;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableList;

import fi.livi.digitraffic.meri.model.CodeDescriptionJson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="All metadata associated with port calls")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class PortsAndBerthsJson {
    @ApiModelProperty(value = "Timestamp when port calls were last updated", required = true)
    public final ZonedDateTime lastUpdated;

    @ApiModelProperty(value = "All UN/LOCODEs(ports)", required = true)
    public final List<SsnLocationJson> locations;
    @ApiModelProperty(value = "All port areas", required = true)
    public final List<PortAreaJson> portAreas;
    @ApiModelProperty(value = "All berths", required = true)
    public final List<BerthJson> berths;

    @ApiModelProperty(value = "All cargo type descriptions", required = true)
    public final List<CodeDescriptionJson> cargoTypes;
    @ApiModelProperty(value = "All vessel type descriptions", required = true)
    public final List<CodeDescriptionJson> vesselTypes;
    @ApiModelProperty(value = "All agent type descriptions", required = true)
    public final List<CodeDescriptionJson> agentTypes;

    public PortsAndBerthsJson(final Instant lastUpdated, final List<SsnLocationJson> locations, final List<PortAreaJson> portAreas,
                              final List<BerthJson> berths,
                              final List<CodeDescriptionJson> cargoTypes, final List<CodeDescriptionJson> vesselTypes,
                              final List<CodeDescriptionJson> agentTypes) {
        this.lastUpdated = lastUpdated == null ? null : lastUpdated.atZone(ZoneId.systemDefault());
        this.cargoTypes = ImmutableList.copyOf(cargoTypes);
        this.vesselTypes = ImmutableList.copyOf(vesselTypes);
        this.agentTypes = ImmutableList.copyOf(agentTypes);
        this.locations = ImmutableList.copyOf(locations);
        this.portAreas = ImmutableList.copyOf(portAreas);
        this.berths = ImmutableList.copyOf(berths);
    }

    public PortsAndBerthsJson(final Instant lastUpdated, final SsnLocationJson ssnLocation, final List<PortAreaJson> portAreas, final List<BerthJson> berths) {
        this(lastUpdated, ssnLocation == null ? emptyList() : Collections.singletonList(ssnLocation), portAreas, berths, emptyList(), emptyList(), emptyList());
    }
}
