package fi.livi.digitraffic.meri.model.winternavigation;

import java.time.ZonedDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ShipPlannedActivity")
public class ShipPlannedActivityProperty {

    @ApiModelProperty(value = "PLA = Planned assistance " +
                              "CCH = Crew change " +
                              "BUN = Bunkering")
    public final String activityType;

    @ApiModelProperty(value = "See \"activityType\"")
    public final String activityText;

    @ApiModelProperty(value = "The target of the icebreaker plan")
    public final String plannedVesselPK;

    @ApiModelProperty(value = "The icebreaker that is making this plan")
    public final String planningVesselPK;

    @ApiModelProperty(value = "Ordering of the planned assistances for this icebreaker")
    public final Integer ordering;

    @ApiModelProperty(value = "Free text given for icebreaker's own activities \"crew change\" and \"bunkering\"")
    public final String plannedWhen;

    @ApiModelProperty(value = "Another free text for crew change and bunkering")
    public final String plannedWhere;

    @ApiModelProperty(value = "Icebreaker-given comment about this plan")
    public final String planComment;

    @ApiModelProperty(value = "When the plan was made or changed")
    public final ZonedDateTime planTimestamp;

    @ApiModelProperty(value = "When the plan was actualized (only available when retrieving past, historical events)")
    public final ZonedDateTime planTimestampRealized;

    @ApiModelProperty(value = "If canceled, when")
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
