package fi.livi.digitraffic.meri.service.winternavigation.dto;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShipPlannedActivityDto {

    public final String activityType;

    public final String activityText;

    public final String plannedVesselPK;

    public final String planningVesselPK;

    public final Integer ordering;

    public final String plannedWhen;

    public final String plannedWhere;

    public final String planComment;

    public final ZonedDateTime planTimestampRealized;

    public final ZonedDateTime planTimestampCanceled;

    public ShipPlannedActivityDto(@JsonProperty("activityType") final String activityType,
                                  @JsonProperty("activityText") final String activityText,
                                  @JsonProperty("planned_vessel_pk") final String plannedVesselPK,
                                  @JsonProperty("planning_vessel_pk") final String planningVesselPK,
                                  @JsonProperty("ordering") final Integer ordering,
                                  @JsonProperty("planned_when") final String plannedWhen,
                                  @JsonProperty("planned_where") final String plannedWhere,
                                  @JsonProperty("plan_comment") final String planComment,
                                  @JsonProperty("plan_timestamp_realized") final ZonedDateTime planTimestampRealized,
                                  @JsonProperty("plan_timestamp_canceled") final ZonedDateTime planTimestampCanceled) {
        this.activityType = activityType;
        this.activityText = activityText;
        this.plannedVesselPK = plannedVesselPK;
        this.planningVesselPK = planningVesselPK;
        this.ordering = ordering;
        this.plannedWhen = plannedWhen;
        this.plannedWhere = plannedWhere;
        this.planComment = planComment;
        this.planTimestampRealized = planTimestampRealized;
        this.planTimestampCanceled = planTimestampCanceled;
    }
}
