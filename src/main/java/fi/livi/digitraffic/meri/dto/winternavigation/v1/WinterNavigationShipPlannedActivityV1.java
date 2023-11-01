package fi.livi.digitraffic.meri.dto.winternavigation.v1;

import java.time.ZonedDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public class WinterNavigationShipPlannedActivityV1 {

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

    public WinterNavigationShipPlannedActivityV1(final String activityType, final String activityText, final String plannedVesselPK, final String planningVesselPK, final Integer ordering,
                                                 final String plannedWhen, final String plannedWhere, final String planComment, final ZonedDateTime planTimestamp,
                                                 final ZonedDateTime planTimestampRealized, final ZonedDateTime planTimestampCanceled) {
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
