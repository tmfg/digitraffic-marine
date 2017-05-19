package fi.livi.digitraffic.meri.service.winternavigation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WinterNavigationShipDto {

    public final String vesselPk;

    public final String vesselSource;

    public final WinterNavigationShipDataDto shipData;

    public final WinterNavigationShipStateDto shipState;

    public final WinterNavigationShipVoyageDto shipVoyage;

    public final List<WinterNavigationShipActivityDto> shipActivities;

    public final List<WinterNavigationShipPlannedActivityDto> plannedActivities;

    public WinterNavigationShipDto(@JsonProperty("vessel_pk") final String vesselPk,
                                   @JsonProperty("vessel_source") final String vesselSource,
                                   @JsonProperty("shipData") final WinterNavigationShipDataDto shipData,
                                   @JsonProperty("shipState") final WinterNavigationShipStateDto shipState,
                                   @JsonProperty("shipVoyage") final WinterNavigationShipVoyageDto shipVoyage,
                                   @JsonProperty("shipActivities") final List<WinterNavigationShipActivityDto> shipActivities,
                                   @JsonProperty("plannedActivities") final List<WinterNavigationShipPlannedActivityDto> plannedActivities) {
        this.vesselPk = vesselPk;
        this.vesselSource = vesselSource;
        this.shipData = shipData;
        this.shipState = shipState;
        this.shipVoyage = shipVoyage;
        this.shipActivities = shipActivities;
        this.plannedActivities = plannedActivities;
    }
}
