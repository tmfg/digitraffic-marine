package fi.livi.digitraffic.meri.model.winternavigation;

import java.sql.Timestamp;

public class ShipActivityProperty {

    public final String activityType;

    public final String activityText;

    public final String activityComment;

    public final Timestamp beginTime;

    public final Timestamp endTime;

    public final Timestamp timestampBegin;

    public final Timestamp timestampEnd;

    public final Timestamp timestampCanceled;

    public final String operatingIcebreakerPK;

    public final String operatingIcebreakerName;

    public final String operatedVesselPK;

    public final String operatedVesselName;

    public final Integer convoyOrder;

    public ShipActivityProperty(String activityType, String activityText, String activityComment, Timestamp beginTime, Timestamp endTime,
                                Timestamp timestampBegin, Timestamp timestampEnd, Timestamp timestampCanceled, String operatingIcebreakerPK,
                                String operatingIcebreakerName, String operatedVesselPK, String operatedVesselName, Integer convoyOrder) {
        this.activityType = activityType;
        this.activityText = activityText;
        this.activityComment = activityComment;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.timestampBegin = timestampBegin;
        this.timestampEnd = timestampEnd;
        this.timestampCanceled = timestampCanceled;
        this.operatingIcebreakerPK = operatingIcebreakerPK;
        this.operatingIcebreakerName = operatingIcebreakerName;
        this.operatedVesselPK = operatedVesselPK;
        this.operatedVesselName = operatedVesselName;
        this.convoyOrder = convoyOrder;
    }
}
