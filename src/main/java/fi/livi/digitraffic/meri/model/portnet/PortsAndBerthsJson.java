package fi.livi.digitraffic.meri.model.portnet;

import java.util.List;

import com.google.common.collect.ImmutableList;

import fi.livi.digitraffic.meri.model.CodeDescriptionJson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="All metadata associated with port calls")
public final class PortsAndBerthsJson {
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

    public PortsAndBerthsJson(final List<SsnLocationJson> locations, final List<PortAreaJson> portAreas, final List<BerthJson> berths,
                              List<CodeDescriptionJson> cargoTypes, List<CodeDescriptionJson> vesselTypes, List<CodeDescriptionJson> agentTypes) {
        this.cargoTypes = ImmutableList.copyOf(cargoTypes);
        this.vesselTypes = ImmutableList.copyOf(vesselTypes);
        this.agentTypes = ImmutableList.copyOf(agentTypes);
        this.locations = ImmutableList.copyOf(locations);
        this.portAreas = ImmutableList.copyOf(portAreas);
        this.berths = ImmutableList.copyOf(berths);
    }
}
