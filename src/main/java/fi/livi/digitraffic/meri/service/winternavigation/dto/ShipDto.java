package fi.livi.digitraffic.meri.service.winternavigation.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShipDto {

    public final String vesselPk;

    public final String vesselSource;

    public final ShipDataDto shipData;

    public final ShipStateDto shipState;

    public final ShipVoyageDto shipVoyage;

    public final List<ShipActivityDto> shipActivities;

    public final List<ShipPlannedActivityDto> plannedActivities;

    public ShipDto(@JsonProperty("vessel_pk") final String vesselPk,
                   @JsonProperty("vessel_source") final String vesselSource,
                   @JsonProperty("shipData") final ShipDataDto shipData,
                   @JsonProperty("shipState") final ShipStateDto shipState,
                   @JsonProperty("shipVoyage") final ShipVoyageDto shipVoyage,
                   @JsonProperty("shipActivities") final List<ShipActivityDto> shipActivities,
                   @JsonProperty("plannedActivities") final List<ShipPlannedActivityDto> plannedActivities) {
        this.vesselPk = vesselPk;
        this.vesselSource = vesselSource;
        this.shipData = shipData;
        this.shipState = shipState;
        this.shipVoyage = shipVoyage;
        this.shipActivities = shipActivities;
        this.plannedActivities = plannedActivities;
    }
}
