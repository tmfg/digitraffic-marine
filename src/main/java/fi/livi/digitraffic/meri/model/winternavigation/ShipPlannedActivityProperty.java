package fi.livi.digitraffic.meri.model.winternavigation;

import java.sql.Timestamp;

public class ShipPlannedActivityProperty {

    public final String activityType;

    public final String activityText;

    public final String plannedVesselPK;

    public final String planningVesselPK;

    public final Integer ordering;

    public final String plannedWhen;

    public final String plannedWhere;

    public final String planComment;

    public final Timestamp planTimestampRealized;

    public final Timestamp planTimestampCanceled;

    public ShipPlannedActivityProperty(String activityType, String activityText, String plannedVesselPK, String planningVesselPK, Integer ordering,
                                       String plannedWhen, String plannedWhere, String planComment, Timestamp planTimestampRealized,
                                       Timestamp planTimestampCanceled) {
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
