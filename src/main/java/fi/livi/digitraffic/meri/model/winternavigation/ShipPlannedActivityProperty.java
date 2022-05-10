package fi.livi.digitraffic.meri.model.winternavigation;

import java.time.ZonedDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ShipPlannedActivity")
public class ShipPlannedActivityProperty {

    @Schema(description = "PLA = Planned assistance " +
                              "CCH = Crew change " +
                              "BUN = Bunkering")
    public final String activityType;

    @Schema(description = "See \"activityType\"")
    public final String activityText;

    @Schema(description = "The target of the icebreaker plan")
    public final String plannedVesselPK;

    @Schema(description = "The icebreaker that is making this plan")
    public final String planningVesselPK;

    @Schema(description = "Ordering of the planned assistances for this icebreaker")
    public final Integer ordering;

    @Schema(description = "Free text given for icebreaker's own activities \"crew change\" and \"bunkering\"")
    public final String plannedWhen;

    @Schema(description = "Another free text for crew change and bunkering")
    public final String plannedWhere;

    @Schema(description = "Icebreaker-given comment about this plan")
    public final String planComment;

    @Schema(description = "When the plan was made or changed")
    public final ZonedDateTime planTimestamp;

    @Schema(description = "When the plan was actualized (only available when retrieving past, historical events)")
    public final ZonedDateTime planTimestampRealized;

    @Schema(description = "If canceled, when")
    public final ZonedDateTime planTimestampCanceled;

    public ShipPlannedActivityProperty(String activityType, String activityText, String plannedVesselPK, String planningVesselPK, Integer ordering,
                                       String plannedWhen, String plannedWhere, String planComment, ZonedDateTime planTimestamp,
                                       ZonedDateTime planTimestampRealized, ZonedDateTime planTimestampCanceled) {
        this.activityType = activityType;
        this.activityText = activityText;
        this.plannedVesselPK = plannedVesselPK;
        this.planningVesselPK = planningVesselPK;
        this.ordering = ordering;
        this.plannedWhen = plannedWhen;
        this.plannedWhere = plannedWhere;
        this.planComment = planComment;
        this.planTimestamp = planTimestamp;
        this.planTimestampRealized = planTimestampRealized;
        this.planTimestampCanceled = planTimestampCanceled;
    }
}
